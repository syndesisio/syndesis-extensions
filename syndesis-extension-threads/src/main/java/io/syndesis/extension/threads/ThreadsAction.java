package io.syndesis.extension.threads;

import java.util.Map;
import java.util.Optional;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

@Action(id = "threads", name = "Threads", description = "Use the Threads EIP", tags = { "threads", "extension", "eip"})
public class ThreadsAction implements Step {
    
    @ConfigurationProperty(
        name = "poolsize",
        displayName = "Pool Size",
        description = "Minimum number of threads in the pool (and initial pool size)",
        type = "int" ,
        required = true)
    private int poolsize;
    
    @ConfigurationProperty(
        name = "maxpoolsize",
        displayName = "Maximum Pool size",
        description = "Maximum number of threads in the pool",
        type = "int" ,
        required = true)
    private int maxPoolSize;
    
    @ConfigurationProperty(
        name = "threadname",
        displayName = "Thread Name",
        description = "The Thread name",
        type = "string" ,
        required = true)
    private String threadName;

	public int getPoolsize() {
		return poolsize;
	}

	public void setPoolsize(int poolsize) {
		this.poolsize = poolsize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	@Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext context, ProcessorDefinition<?> route, Map<String, Object> parameters) {
        ObjectHelper.notNull(route, "route");
        ObjectHelper.notNull(poolsize, "poolsize");
        ObjectHelper.notNull(maxPoolSize, "maxpoolsize");
        ObjectHelper.notNull(threadName, "threadname");

        return Optional.of(route.threads(poolsize, maxPoolSize, threadName));
    }
}
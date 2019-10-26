package io.syndesis.extension.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.util.ObjectHelper;

import twitter4j.JSONObject;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

public class TweetMedia {

	private User user;
	private Date createdAt;
	private MediaEntity[] mediaEntities;
	
	public TweetMedia(User user, Date timestamp, MediaEntity[] mediaEntities) {
		this.user = user;
		this.createdAt = timestamp;
		this.mediaEntities = mediaEntities;
	}
	
	public TweetMedia(Status status) {
		if (!ObjectHelper.isEmpty(status)) {
			this.user = status.getUser();
			this.createdAt = status.getCreatedAt();
			this.mediaEntities = status.getMediaEntities();
		}
	}

	/**
	 * Builds valid JSON 
	 * 
	 * 
	 * @return
	 */
	public JSONObject toJSON() {
		JSONObject json = null;
		
		if (ObjectHelper.isNotEmpty(this.user) &&  ObjectHelper.isNotEmpty(this.createdAt) && ObjectHelper.isNotEmpty(mediaEntities)) {
			json = new JSONObject();
			json.put("twitterID", this.user.getId());
			json.put("tweetCreatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createdAt));
			
			
			for (MediaEntity mediaEntity : mediaEntities) {
				json.append("url", mediaEntity.getMediaURL());
			}
		}
		return json;
	}
	
	@Override
	public String toString() {
		JSONObject json = toJSON();
		return ObjectHelper.isNotEmpty(json) ? json.toString() : null;
	}
}

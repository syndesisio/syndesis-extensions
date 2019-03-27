# Charset Conversion Extension

This is a simple Syndesis Extension to convert text from one charset to another. For example, it can be used to convert from ASCII to EBCDIC and vice-versa.

## ASCII To EBCDIC Converter

The action exposes one configurable field:
- **Character Set** The type of EBCDIC character set to use

![Screenshot](ascii-to-ebcdic-screenshot.png)

## EBCDIC To ASCII Converter

The action exposes one configurable field:
- **Character Set** The type of EBCDIC character set to use

![Screenshot](ebcdic-to-ascii-screenshot.png)

## Generic Character Set Converter

The action exposes two configurable fields:
- **Input Character Set** The character set of the input text
- **Output Character Set** The character set of the output text

![Screenshot](generic-converter-screenshot.png)

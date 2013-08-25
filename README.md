XPA
===

Framework for XML processing for Android applications

The XPA framework (XML processing for Android) describes the annotation driven xml serialization and deserialization. It provides XML-to-Object and Object-to-XML mapping based on the Object's annotation configuration.

Projects:

	XPA-Application - example application how to use the API
	XPA-Framework - the framework itself
	XPA-Server - the server side example to attach the example application to test XML via http requests/resposnes

Framework Description
===

The XPA framework is based on stream handlin annotation driven XML creation and parsing. The mapping XML document to object is provided by four based annotations:
  
  XmlRootElement - marks the class as the root element of the targeted XML document
  XmlElement - annotation marking the field as element
  XmlAttribute - annotation marking the field as attribute
  XmlValue - annotation marking the field as element value.


Commits
===

Types of commits marking commit type:
- NEW - new features or functionality
- FIX - fixed bugs and errors on projects
- UPD - functionality updates
- DOC - added documentation
- MISC - various oncategorized commits

Note: There can be more than one commit type pre commit used (e.g.: FIX+DOC, ...).

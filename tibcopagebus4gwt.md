This page provides a tutorial to integrate the gwt-jsonmaker with tibcopagebus4gwt (http://code.google.com/p/tibcopagebus4gwt/).

# Introduction #

tibcopagebus4gwt is a messaging tool with supports publisher-subscriber model. This is particularly helpful in a page with mashups, when one message needs to be delivered to a number of widgets.

It is also helpful to reduce the dependencies among the widgets. Its publisher-subscriber model ensures that publishers would never know who the subscribers are and vice versa. Thus, subscriber specific code never finds its way into publisher and likewise for subscribers

# Details #

Tibcopagebus4gwt makes use of gwt-jsonizer(http://code.google.com/p/gwt-jsonizer) to convert Java beans to Json which is then transferred as the message. This json is then parsed into Java Bean, again usinfg gwt-jsonizer. Since gwt-jsonizer has not been updated for GWT 2.0+, the same task can be accomplished using gwt-jsonmaker. You can find the tibcopagebus example in the downloads at http://code.google.com/p/gwt-jsonmaker/downloads/detail?name=TibcoPageBusTest.zip&can=2&q=

# How to use tibcopagebus4gwt with jsonmaker? #

## 1. Define a message bean ##
```
public class MessageBean {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
```

## 2. Create message bean jsonizer ##
```
public interface MessageBeanJsonizer extends Jsonizer {}
```

## 3. Register a unique string to publish / subscribe messages. ##
It is a good practice to have this string as the fully qualified path of MessageBean
```
try {
     pageBusAdapter.PageBusSubscribe(  "com.tibcopagebus.jsonmaker.gwt.client.beans.MessageBean", null, null, null, null);
} catch (PageBusAdapterException e) {
	e.printStackTrace();
}
```
## 4. Create PageBus Subscriber Callback Listener ##
```
pageBusAdapter.addPageBusSubscriptionCallbackListener(new PageBusListener(){
	@Override
	public String getName() {
		return "com.tibcopagebus.jsonmaker.gwt.client.beans.MessageBean";
	}
	@Override
	public void onPageBusSubscriptionCallback(PageBusEvent event) {
		try {
//jsonmaker parser is used internally to parse the json
		MessageBean message = (MessageBean)event.getMessage( (Jsonizer)GWT.create(MessageBeanJsonizer.class));
//messageShow is a label
		messageShow.setText(message.getMessage());
		} catch (PageBusAdapterException e) {
			e.printStackTrace();
		}
}});
```
## 5. On an event publish the message ##
```
//sendMessage is a button
sendMessage.addClickHandler(new ClickHandler(){
	@Override
	public void onClick(ClickEvent event) {
		MessageBean message = new MessageBean();
//messageTb is a textbox
		message.setMessage(messageTb.getText());
		try {
//Jsonmaker is used internally to convert Message bean to json
pageBusAdapter.PageBusPublish("com.tibcopagebus.jsonmaker.gwt.client.beans.MessageBean",
message, (MessageBeanJsonizer) GWT.create(MessageBeanJsonizer.class));
		} catch (PageBusAdapterException e) {
			e.printStackTrace();
		}
}});
```
## 6. Setup project.gwt.xml file ##
Add following lines to project.gwt.xml file
```
<inherits name="com.eliasbalasis.tibcopagebus4gwt.tibcopagebus4gwt"/>
<inherits name="org.jsonmaker.gwt.Gwt_jsonmaker" />
```
## 7. Include pagebus.js in html and init the pagebus ##
```
<script type="text/javascript" language="javascript" src="pagebus.js"></script>
<script type="text/javascript">
   if(window.parent.PageBus) {
     	window.PageBus = window.parent.PageBus;
   }
</script>
```
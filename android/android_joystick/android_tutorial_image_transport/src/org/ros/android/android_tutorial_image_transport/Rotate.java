package org.ros.android.android_tutorial_image_transport;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;


public class Rotate extends AbstractNodeMain {
    private String topic_name;
    private Integer message;

    public Rotate() {
        topic_name = "chatter";
    }

    public Rotate(String topic) {
        topic_name = topic;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/rotate");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        final Publisher<std_msgs.Int32> publisher =
                connectedNode.newPublisher(topic_name, std_msgs.Int32._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {
                sequenceNumber = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                std_msgs.Int32 val = publisher.newMessage();
                val.setData(message);
                publisher.publish(val);
            }
        });
    }

}
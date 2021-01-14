package com.luizalabs.pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.TopicName;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class PubSubInit {
    public static void main(String[] args) throws IOException {
        // [START pubsub_use_emulator]
        String hostport = System.getenv("PUBSUB_EMULATOR_HOST");
        String project = System.getenv("PUBSUB_PROJECT");
        String topic = System.getenv("PUBSUB_TOPIC");
        String subscriptionName = System.getenv("PUBSUB_SUBSCRIPTION");

        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forTarget(hostport);
        channelBuilder.usePlaintext();
        ManagedChannel channel = channelBuilder.build();
        try {
            TransportChannelProvider channelProvider = FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));

            CredentialsProvider credentialsProvider = NoCredentialsProvider.create();

            // Set the channel and credentials provider when creating a `TopicAdminClient`.
            // Similarly for SubscriptionAdminClient
            TopicAdminClient topicClient =
                    TopicAdminClient.create(
                            TopicAdminSettings.newBuilder()
                                    .setTransportChannelProvider(channelProvider)
                                    .setCredentialsProvider(credentialsProvider)
                                    .build());

            ProjectTopicName topicName = ProjectTopicName.of(project, topic);
            topicClient.createTopic(topicName);


            ProjectSubscriptionName subscription = ProjectSubscriptionName.of(project, subscriptionName);

            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(SubscriptionAdminSettings.newBuilder()
                    .setTransportChannelProvider(channelProvider)
                    .setCredentialsProvider(credentialsProvider).build());

            subscriptionAdminClient.createSubscription(subscription, topicName, PushConfig.getDefaultInstance(), 0);
        } finally {
            channel.shutdown();
        }
        // [END pubsub_use_emulator]
    }
}

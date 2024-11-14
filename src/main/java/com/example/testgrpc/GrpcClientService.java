package com.example.testgrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    @Bean
    public CommandLineRunner run(UserServiceGrpc.UserServiceBlockingStub userServiceStub) {
        return args -> {
            int userId = createUser(userServiceStub, UserOuterClass.User.newBuilder()
                    .setRole("USER")
                    .setUsername("test_username")
                    .setName("test_name")
                    .setPassword("123")
                    .build());
            getUser(userServiceStub, userId);
            updateUser(userServiceStub, UserOuterClass.User.newBuilder()
                    .setId(userId)
                    .setName("changed_name")
                    .build());
            getUser(userServiceStub, userId);
            deleteUser(userServiceStub, userId);
        };
    }

    @Bean
    public ManagedChannel channel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceStub(ManagedChannel channel) {
        return UserServiceGrpc.newBlockingStub(channel);
    }

    private static int createUser(UserServiceGrpc.UserServiceBlockingStub stub, UserOuterClass.User user) {
        UserOuterClass.CreateUserRequest request = UserOuterClass.CreateUserRequest.newBuilder()
                .setUser(user)
                .build();
        UserOuterClass.CreateUserResponse response = stub.createUser(request);

        System.out.println("Created user: " + response);
        return response.getId();
    }

    private static void updateUser(UserServiceGrpc.UserServiceBlockingStub stub, UserOuterClass.User user) {
        UserOuterClass.UpdateUserRequest request = UserOuterClass.UpdateUserRequest.newBuilder()
                .setUser(user)
                .build();
        UserOuterClass.UpdateUserResponse response = stub.updateUser(request);

        System.out.println("Created user: " + response);
    }

    private static void getUser(UserServiceGrpc.UserServiceBlockingStub stub, Integer id) {
        UserOuterClass.GetUserRequest request = UserOuterClass.GetUserRequest.newBuilder()
                .setId(id)
                .build();
        UserOuterClass.GetUserResponse response = stub.getUser(request);

        System.out.println("Get user: " + response);
    }

    private static void deleteUser(UserServiceGrpc.UserServiceBlockingStub stub, Integer id) {
        UserOuterClass.DeleteUserRequest request = UserOuterClass.DeleteUserRequest.newBuilder()
                .setId(id)
                .build();
        UserOuterClass.DeleteUserResponse response = stub.deleteUser(request);

        System.out.println("Get user: " + response);
    }

}

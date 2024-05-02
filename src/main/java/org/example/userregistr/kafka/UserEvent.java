package org.example.userregistr.kafka;

public record UserEvent(String email, String password) implements Event{

}

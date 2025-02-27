package dev.domain;

public class Member {
    private int id;

    public int getId() {
        return id;
    }

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member(int id, String name, String password) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
    }
    @Override
    public String toString() {
        return "Member [name=" + name + "]";
    }
}

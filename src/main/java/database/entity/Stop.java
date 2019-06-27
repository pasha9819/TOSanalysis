package database.entity;

public class Stop {
    private Integer KS_ID;

    public Integer getId() {
        return KS_ID;
    }

    public Stop(){}

    public Stop(int id){
        KS_ID = id;
    }
}

public class TVRemote {
    public static void main(String[] args) {
        TVContext tv = new TVContext();

        State stateStart = new TVStartState();
        State stateStop = new TVStopState();

        tv.setTvState(stateStart);
        tv.doAction();

        tv.setTvState(stateStop);
        tv.doAction();
    }
}

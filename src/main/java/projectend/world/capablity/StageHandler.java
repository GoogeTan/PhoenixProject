package projectend.world.capablity;

public class StageHandler implements IStager
{
    private int stage = 1;
    private int stageIn = 1;
    @Override
    public int getStage() {return stage;}

    @Override
    public void addStage(){ stage++;}

    @Override
    public void setStage(int stageIn) {stage = stageIn;}

    @Override
    public int getStageIn() {return stageIn;}

    @Override
    public void addStageIn(){ stageIn++;}

    @Override
    public void setStageIn(int stageInput) {stageIn = stageInput;}
}

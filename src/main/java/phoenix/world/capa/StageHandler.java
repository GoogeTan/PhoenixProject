package phoenix.world.capa;

public class StageHandler implements IStager
{
    private int stage;
    private int stageIn;

    public StageHandler()
    {
        stage  = 1;
        stageIn = 1;
    }

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

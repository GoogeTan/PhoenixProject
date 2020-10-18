package statemachine.client.models;

import java.util.ArrayList;

public abstract class PartList
{
    public ArrayList<PartRenderer> parts;
    private ArrayList<PartRenderer> masterParts;

    public PartList(ArrayList<PartRenderer> parts)
    {
        this.parts = parts;
        for (PartRenderer renderer : parts)
            if(renderer.isMaster)
                masterParts.add(renderer);
    }

    public ArrayList<PartRenderer> getMasterParts()
    {
        return masterParts;
    }
}

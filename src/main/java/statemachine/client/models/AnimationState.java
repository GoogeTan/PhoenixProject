package statemachine.client.models;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class AnimationState<T extends PartList>
{
    ArrayList<Pair<TransitionRotationScale, PartRenderer>> transitionsRotationList;

    public AnimationState(ArrayList<Pair<TransitionRotationScale, PartRenderer>> transitionsRotationList)
    {
        this.transitionsRotationList = transitionsRotationList;
    }

    public void applyRotationsTo(T part)
    {

    }

    //todo
    public AnimationState<T> blend(AnimationState<T> second, double alpha)
    {
        ArrayList<Pair<TransitionRotationScale, PartRenderer>> res = new ArrayList<>();
        for (int i = 0; i < transitionsRotationList.size(); ++i)
        {
            res.add(
                    Pair.of
                            (
                                    (
                                            second.transitionsRotationList.get(i).getLeft().blend(transitionsRotationList.get(i).getLeft())
                                    ),
                                    second.transitionsRotationList.get(i).getRight()
                            )
                    );
        }
        return new AnimationState<>(res);
    }

    protected ArrayList<Pair<TransitionRotationScale, PartRenderer>> getTRSs()
    {
        return transitionsRotationList;
    }
}

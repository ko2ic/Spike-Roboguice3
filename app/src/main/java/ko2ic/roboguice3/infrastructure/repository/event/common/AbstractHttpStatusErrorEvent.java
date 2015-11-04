package ko2ic.roboguice3.infrastructure.repository.event.common;


public abstract class AbstractHttpStatusErrorEvent {

    private int mStatudCode;

    public AbstractHttpStatusErrorEvent(int statusCode){
        this.mStatudCode = statusCode;
    }

    public boolean isNotFound(){
        if(mStatudCode == 404){
            return true;
        }
        return false;
    }

    // TODO; このメソッドは本当は必要ない
    public int getStatusCode(){
        return mStatudCode;
    }
}

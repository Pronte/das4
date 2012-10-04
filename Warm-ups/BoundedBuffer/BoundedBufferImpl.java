/**
 * Implementation of the BoundedBuffer interface
 */
import Java.util.*;

public class BoundedBufferImpl implements BoundedBuffer {

	public final int DEFAULT_SIZE = 5;

	private List<Object> lBuff;	
	private int size;

	public BoundedBufferImpl (int bSize) {
		lBuff = new ArrayList<Object>(bSize);	
		this.size = bSize;
		lock = new Object();
	}

	public BoundedBufferImpl () {
		BoundedBufferImpl(DEFAULT_SIZE);
	}

	
    /**
     * method to insert "o" at the end of the end of the buffer
     * if no room, must wait until another thread has invoked remove()
     * this method cannot be synchronized,
	* since syncronyzed+wait() can lead to a deadlock.
     * @param o Object to insert
     */ 
    public void insert(Object o){
		while(lBuff.size() >= bSize){
			try	{ this.wait();}
			catch (InterruptedException e){}
		}
		try{
			addToBuff(o);
		} catch (FullBufferException e){
			insert(o);
		}
	}

    /**
     * method that only controls the actual interaction with the buffer.
	*It's synchronized to avoid concurrency problems
	*/
	private synchronized void addToBuff(Object o) throws FullBufferException{
		if(lBuff.size() < size){
			lBuff.add(o);
			this.notifyAll();
		} else {
			throw new FullBufferException();
		}
	}

    /**
     * method to remove "o" from the beginning of the buffer
     * if empty, must wait until another thread has invoked insert()
     * this method cannot be synchronized,
	* since syncronyzed+wait() can lead to a deadlock.
     * @result Object removed
     */
    	public Object remove(){
		while(lBuff.size() == 0){
			try	{ this.wait();}
			catch (InterruptedException e){}
		}
		try{ return rmFromBuff();}
		catch(EmptyBufferException e){
			return remove();
		}
	}


    /**
     * method that only controls the actual interaction with the buffer.
	*It's synchronized to avoid concurrency problems
	*/	
	private synchronized Object rmFromBuffer() throws EmptyBufferException{
		if(lBuff.size() > 0){
			return lbuff.remove(0);
		} else {
			throw new EmptyBufferException();
		}
	
	}

}

class FullBufferException extends Exception {}
class EmptyBufferException extends Exception {}

/* Timothy Cartmel
This programs compares the efficiencies
of different disk algorithms. */
package osch10massstorage;
import java.lang.Math;
import java.util.Random;
import java.util.Arrays;

/**
 *
 * @author timca
 */
public class OSCh10MassStorage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int startPos = 0;
        if (args.length > 0) {
            try {
                startPos = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
            System.exit(1);
            }
        }
        int[] cyclinderReq = new int[1000];    

	for (int i = 0; i <= 999; ++i)	
            cyclinderReq[i] = new Random().nextInt(4999);

	int totalSpins1 = FCFS(cyclinderReq, startPos);
	System.out.printf("FCFS Head Moves: %d\n", totalSpins1);

	int totalSpins2 = SSTF(cyclinderReq, startPos);
	System.out.printf("SSTF Head Moves: %d\n", totalSpins2);

	int totalSpins3 = SCAN(cyclinderReq, startPos);
	System.out.printf("SCAN Head Moves: %d\n", totalSpins3);

	int totalSpins4 = CSCAN(cyclinderReq, startPos);
	System.out.printf("CSCAN Head Moves: %d\n", totalSpins4);

	int totalSpins5 = LOOK(cyclinderReq, startPos);
	System.out.printf("LOOK Head Moves: %d\n", totalSpins5);

	int totalSpins6 = CLOOK(cyclinderReq, startPos);
	System.out.printf("CLOOK Head Moves: %d\n", totalSpins6);
	
	
}

static int FCFS(int cyclinderReq[], int startPos){
    int totalMoves = 0;
    int prevPos = startPos;		
    for(int i = 0; i < 1000; i++){
	int diffNum;	
	diffNum = cyclinderReq[i] - prevPos;
	totalMoves += Math.abs(diffNum);
        prevPos = cyclinderReq[i];
    }
    return totalMoves;	
}

static int SSTF(int cyclinderReq[], int startCyl){
    int[] tempCyclinderReq = new int[1000];    
    int totalMoves = 0;
    int prevCyl = startCyl;
    int l = 999;
    int r = 1000;
    System.arraycopy(cyclinderReq, 0, tempCyclinderReq, 0, 1000);
    Arrays.sort(tempCyclinderReq);
    for(int i = 0; i < 1000; i++){
        if(tempCyclinderReq[i] >= startCyl){
            l = i - 1;
            r = i;
            break;
        }       
    }
    while(true){
        int diff1 = 100000;
        int diff2 = 100000;
        if(l>=0)
            diff1 = prevCyl - tempCyclinderReq[l];
        if(r<1000)
            diff2 = tempCyclinderReq[r] - prevCyl;
        
        if(diff1 == 100000 && diff2 == 100000)
            break;
        
        if(diff1 < diff2){
            totalMoves += diff1;
            prevCyl = tempCyclinderReq[l];
            l--;
        }    
        else if(diff2 < diff1){
            totalMoves += diff2;
            prevCyl = tempCyclinderReq[r];
            r++;
        }
        
    }
    
    return totalMoves;
}

static int SCAN(int cyclinderReq[], int startCyl){
    int[] tempCyclinderReq = new int[1000];    
    int totalMoves = 0;
    int prevCyl = startCyl;
    int l = 999;
    int r = 1000;
    System.arraycopy(cyclinderReq, 0, tempCyclinderReq, 0, 1000);
    Arrays.sort(tempCyclinderReq);
    for(int i = 0; i < 1000; i++){
        if(tempCyclinderReq[i] >= startCyl){
            l = i - 1;
            r = i;
            break;
        }       
    }
    int diff1 = 100000;
    int diff2 = 100000;
    int dir = 0;
    if(l>=0)
        diff1 = prevCyl - tempCyclinderReq[l];
    if(r<1000)
        diff2 = tempCyclinderReq[r] - prevCyl;
           
    if(diff1 < diff2)
        dir = -1;
    else if(diff2 <= diff1)
        dir = 1;
    
    while(l >= 0 || r < 1000){
        if(dir > 0){
            diff2 = tempCyclinderReq[r] - prevCyl;
            totalMoves += diff2;
            prevCyl = tempCyclinderReq[r];
            if(++r >= 1000 && l >= 0){
                totalMoves += 4999 - prevCyl;
                prevCyl = 4999;
                dir = -1;
            }
        }
        else{
            diff1 = prevCyl - tempCyclinderReq[l];
            totalMoves += diff1;
            prevCyl = tempCyclinderReq[l];
            if(--l < 0 && r < 1000){
                totalMoves += prevCyl - 0; 
                prevCyl = 0;
                dir = 1;
            }
        }
    }
    return totalMoves;
}

static int CSCAN(int cyclinderReq[], int startCyl){
    int[] tempCyclinderReq = new int[1000];    
    int totalMoves = 0;
    int prevCyl = startCyl;
    int l = 0;
    int r = 1000;
    System.arraycopy(cyclinderReq, 0, tempCyclinderReq, 0, 1000);
    Arrays.sort(tempCyclinderReq);
    for(int i = 0; i < 1000; i++){
        if(tempCyclinderReq[i] >= startCyl){
            r = i;
            break;
        }       
    }
    int diff;
    int dir = 1;
    int startIdx = r;
    while(dir != 0){
        if(dir > 0){
            diff = tempCyclinderReq[r] - prevCyl;
            totalMoves += diff;
            prevCyl = tempCyclinderReq[r];
            if(++r >= 1000){
                totalMoves += 4999 - prevCyl;
                totalMoves += 5000;
                prevCyl = 0;
                dir = -1;
            }
        }
        else{
            diff = tempCyclinderReq[l] - prevCyl;
            totalMoves += diff;
            prevCyl = tempCyclinderReq[l];
            if(++l >= startIdx)
                dir = 0;
        }
    }
    return totalMoves;
}

static int LOOK(int cyclinderReq[], int startCyl){
    int[] tempCyclinderReq = new int[1000];    
    int totalMoves = 0;
    int prevCyl = startCyl;
    int l = 999;
    int r = 1000;
    System.arraycopy(cyclinderReq, 0, tempCyclinderReq, 0, 1000);
    Arrays.sort(tempCyclinderReq);
    for(int i = 0; i < 1000; i++){
        if(tempCyclinderReq[i] >= startCyl){
            l = i - 1;
            r = i;
            break;
        }       
    }
    int diff1 = 100000;
    int diff2 = 100000;
    int dir = 0;
    if(l>=0)
        diff1 = prevCyl - tempCyclinderReq[l];
    if(r<1000)
        diff2 = tempCyclinderReq[r] - prevCyl;
           
    if(diff1 < diff2)
        dir = -1;
    else if(diff2 <= diff1)
        dir = 1;
    
    while(l>=0 || r < 1000){
        if(dir > 0){
            diff2 = tempCyclinderReq[r] - prevCyl;
            totalMoves += diff2;
            prevCyl = tempCyclinderReq[r];
            if(++r >= 1000)
                dir = -1;
        }
        else{
            diff1 = prevCyl - tempCyclinderReq[l];
            totalMoves += diff1;
            prevCyl = tempCyclinderReq[l];
            if(--l < 0)
                dir = 1;
        }
    }
    return totalMoves;
}

static int CLOOK(int cyclinderReq[], int startCyl){
    int[] tempCyclinderReq = new int[1000];    
    int totalMoves = 0;
    int prevCyl = startCyl;
    int l = 0;
    int r = 1000;
    System.arraycopy(cyclinderReq, 0, tempCyclinderReq, 0, 1000);
    Arrays.sort(tempCyclinderReq);
    for(int i = 0; i < 1000; i++){
        if(tempCyclinderReq[i] >= startCyl){
            r = i;
            break;
        }       
    }
    int diff;
    int dir = 1;
    int startIdx = r;
    while(dir != 0){
        if(dir > 0){
            diff = tempCyclinderReq[r] - prevCyl;
            totalMoves += diff;
            prevCyl = tempCyclinderReq[r];
            if(++r >= 1000){
                totalMoves += prevCyl - tempCyclinderReq[l];
                prevCyl = tempCyclinderReq[l];
                dir = -1;
            }
        }
        else{
            diff = tempCyclinderReq[l] - prevCyl;
            totalMoves += diff;
            prevCyl = tempCyclinderReq[l];
            if(++l >= startIdx)
                dir = 0;
        }
    }
    return totalMoves;
}
}

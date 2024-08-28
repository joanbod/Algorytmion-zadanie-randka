
import java.util.*;

public class Randka {
    int MAX_N=500000,MAX_K=500000,MAX_LOG_N=19;
    int n=0,k=0;
    int sciezka[][]=new int[MAX_N][MAX_LOG_N];
    int odleglosc[]=new int[MAX_N];
    int spojnaNr[]=new int[MAX_N];
    int pozycjaCyklu[]=new int[MAX_N];
    int dlugoscCyklu[]=new int[MAX_N];
    int nrSpojnej=0;
    boolean odwiedzony[]=new boolean[MAX_N];
    boolean obliczony[]=new boolean[MAX_N];
    
    Randka(){
        
    }
    int droga(int v,int kroki){
        for(int i=MAX_LOG_N;i>=0;i--){
            if(1<<i<=kroki){
                v=sciezka[v][i];
                kroki-=1<<i;
            }
        }
        return v;
    }
    
    void poczatekCyklu(int v){
        int w=v,pozycja=0;
        
        do{
            
            spojnaNr[w]=nrSpojnej;
            odleglosc[w]=0;
            obliczony[w]=true;
            pozycjaCyklu[w]=pozycja++;
            w=sciezka[w][0];
            dlugoscCyklu[v]++;
            
        }while(v!=w);
        
        do{
            
            dlugoscCyklu[w]=dlugoscCyklu[v];
            w=sciezka[w][0];
            
        }while(v!=w);
        
        nrSpojnej++;
        
    }
    
    void dfs(int v){
        
        odwiedzony[v]=true;
        
        if(!odwiedzony[sciezka[v][0]]){
            dfs(sciezka[v][0]);
        }
        else if(!obliczony[sciezka[v][0]]){
            poczatekCyklu(sciezka[v][0]);
            return;
        }
        
        if(!obliczony[v]){
            obliczony[v]=true;
            odleglosc[v]=odleglosc[sciezka[v][0]]+1;
            spojnaNr[v]=spojnaNr[sciezka[v][0]];
        }
        
    }
    int[] najlepszaDroga(int a,int b,int c,int d){
        int wynik[]=new int[2];
        if(Math.max(a,b)<Math.max(c,d)){
            wynik[0]=a;
            wynik[1]=b;
            return wynik;
        }
        if(Math.max(a,b)>Math.max(c,d)){
            wynik[0]=c;
            wynik[1]=d;
            return wynik;
        }
        if(Math.min(a,b)<Math.min(c,d)){
            wynik[0]=a;
            wynik[1]=b;
            return wynik;
        }
        if(Math.min(a,b)>Math.min(c,d)){
            wynik[0]=c;
            wynik[1]=d;
            return wynik;
        }
        if(a>c){
            wynik[0]=a;
            wynik[1]=b;
            return wynik;
        }
        wynik[0]=c;
        wynik[1]=d;
        return wynik;
            
    }
    public void glowna() {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Podaj ilosc komor: ");
        n=in.nextInt();
        System.out.println("Podaj ilosc par: ");
        k=in.nextInt();
        
        System.out.println("Podaj sciezki: ");
        for(int i=0;i<n;i++){
            sciezka[i][0]=in.nextInt();
            sciezka[i][0]--;
        }
        
        for(int i=1;i<MAX_LOG_N;i++){
            for(int j=0;j<n;j++){
                 
                //TODO: Indeks spoza listy
                sciezka[j][i]=sciezka[sciezka[j][i-1]][i-1];       
            }
        }
        
        for(int i=0;i<n;i++){
            if(!odwiedzony[i]){
                dfs(i);
            }
        }
        
        while(k!=0){
            k--;
            int krokiM=0,krokiW=0;
            
            System.out.println("Podaj numer komory, w ktorej jest kobieta: ");
            int w=in.nextInt();
            System.out.println("Podaj numer komory, w ktorej jest mezczyzna: ");
            int m=in.nextInt();
            
            w--;
            m--;
            
            if(spojnaNr[w]!=spojnaNr[m]){
                System.out.println("-1 -1");
                continue;
            }
            
            if(odleglosc[w]>odleglosc[m]){
                krokiW=odleglosc[w]-odleglosc[m];
                w=droga(w,krokiW);
                
                
            }
            if(odleglosc[w]<odleglosc[m]){
                krokiM=odleglosc[m]-odleglosc[w];
                m=droga(m,krokiM);
            }
            
            for(int i=MAX_LOG_N-1;i>=0;i--){
                if((1<<i<odleglosc[w])&&(sciezka[w][i]!=sciezka[m][i])){
                    krokiW+=1<<i;
                    krokiM+=1<<i;
                    m=sciezka[m][i];
                    w=sciezka[w][i];
                }
            }
            
            if((odleglosc[w]>=1)&&(w!=m)){
                krokiW++;
                krokiM++;
                m=sciezka[m][0];
                w=sciezka[w][0];
                
            }
            int wynik[]=new int[2];
            if(m!=w){
                if(pozycjaCyklu[w]<pozycjaCyklu[m]){
                    wynik=najlepszaDroga(krokiW+(pozycjaCyklu[m]-pozycjaCyklu[w]),krokiM,krokiW,krokiM+(dlugoscCyklu[w]-(pozycjaCyklu[m]-pozycjaCyklu[w])));
                }
                if(pozycjaCyklu[w]>pozycjaCyklu[m]){
                    wynik=najlepszaDroga(krokiW,krokiM+(pozycjaCyklu[w]-pozycjaCyklu[m]),krokiW+(dlugoscCyklu[m]-(pozycjaCyklu[w]-pozycjaCyklu[m])),krokiM);
                }
                krokiW=wynik[0];
                krokiM=wynik[1];
            }
            
            System.out.println("Para bedzie musial‚a przejsc: "+krokiW+" "+krokiM);
        }
                
       
    }
    
}

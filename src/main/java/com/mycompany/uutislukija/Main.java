
package com.mycompany.uutislukija;

import com.mycompany.hackernewsuutiset.HackerPaivanUutiset;

public class Main {
    public static void main(String[] args){
        HackerPaivanUutiset hakija = new HackerPaivanUutiset();
        if(args.length == 0 || args[0].isEmpty()){
            System.out.println("Et antanut komentoa. Käytettävissä olevat komennot: " + komennot());
        } else if(args[0].equals("viimeisin")){
            System.out.println(hakija.haeViimeisinUutinen());
        } else if(args[0].equals("suosituin")){
            System.out.println(hakija.haeSuosituinUutinen());
        }
    }
    
    public static String komennot(){
        return "Komennot: viimeisin, suosituin";
    }
}

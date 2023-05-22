import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SimEnka {

    public static void main(String[] args) {
        //System.out.println("program je krenuo");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int brojac = 1;
        List<String> list=new ArrayList<String>();
        List<String> ulazni_nizovi=new ArrayList<String>();
        String[] stanja = null ;
        String[] simboli = null;
        String[] prihvatljiva_stanja = null;
        String poc_stanje = null;
        String[] nizovi = null;

        HashMap<String, HashMap<String, List<String>>> prijelazi = null;


        try {
            while ( (line = br.readLine()) != null && !line.isEmpty())
            {
               /* line = br.readLine();
                if(line.isEmpty()  || line.isBlank()) {break;}*/


                //svaki skup nizova je stavljen u listu
                if(brojac == 1) {
                    nizovi = line.split("\\|");
                    brojac++;

                    for(String niz:nizovi) {
                        ulazni_nizovi.add(niz);
                    }
                }

                else if(brojac ==2) {
                    stanja = line.split(",");
                    brojac++;
                }
                else if(brojac ==3) {
                    simboli = line.split(",");
                    brojac++;
                }
                else if(brojac ==4) {
                    prihvatljiva_stanja = line.split(",");
                    brojac++;
                }
                else if(brojac ==5) {
                    poc_stanje = line;
                    brojac++;
                }
                else {
                    String funk_prijelaz = line;

                    list.add(funk_prijelaz);
                    brojac++;
                }

            }

            prijelazi = parsiranje(list,brojac);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        $DKA(ulazni_nizovi,stanja,simboli,prihvatljiva_stanja,poc_stanje,prijelazi);


    }



    static HashMap<String, HashMap<String, List<String>>> parsiranje(List<String> list, int brojac) {

        HashMap<String, HashMap<String,List<String> > > prijelazi = new HashMap<String,HashMap<String,List<String> >>();

        for(String linija : list) {
            String trenutnoStanje = linija.substring(0, linija.indexOf(','));
            String simbolAbecede = linija.substring(linija.indexOf(',')+1, linija.indexOf('-'));
            String skupIducihStanja = linija.substring(linija.indexOf('>')+1) ;

            var stanja_var = skupIducihStanja.split(",");//odvajanje stanja
            List<String> stanja = List.of(stanja_var);//stavljanje pojedinih stanja u listu stringova


            if(prijelazi.get(trenutnoStanje) == null) {
                HashMap<String,List<String>> mapa = new HashMap<String, List<String>>();
                mapa.put(simbolAbecede, stanja);
                prijelazi.put(trenutnoStanje, mapa);
            }
            else {
                HashMap<String,List<String>> mapa2 = prijelazi.get(trenutnoStanje);
                mapa2.put(simbolAbecede, stanja);
                prijelazi.put(trenutnoStanje, mapa2);
            }
        }

        return prijelazi;
    }


    private static void $DKA(List<String> ulazni_nizovi, String[] stanja, String[] simboli,
                             String[] prihvatljiva_stanja, String poc_stanje, HashMap<String, HashMap<String, List<String>>> prijelazi) {


        //vrtit ce se onoliko puta koliko ima ulaznih nizova
        for(String niz : ulazni_nizovi) {
            String[] simbol = niz.split(",");

            if(prijelazi.containsKey(poc_stanje))
            {
                System.out.print(poc_stanje);

                List<String> trenutna_stanja= new ArrayList<String>();
                trenutna_stanja.add(poc_stanje);

                //cita simbole i mijenja listu trenutnih stanja kroz koju mora proc
                for(String sim : simbol)
                {
                    //update stanja
                    trenutna_stanja = epsilon_okruzenje(trenutna_stanja,prijelazi);
                    trenutna_stanja = prijelaz_fja(trenutna_stanja,prijelazi,sim);
                    trenutna_stanja = epsilon_okruzenje(trenutna_stanja,prijelazi);

                    System.out.print("|");
                    //ispis stanja

                    if(trenutna_stanja.size() == 0)
                        System.out.print("#");
                    else {
                        trenutna_stanja.sort(String::compareTo);
                        
                        String proslo_stanje=null;
                        //if(trenutna_stanja.size() > 1) {
                        for (int j = 0; j < trenutna_stanja.size(); j++) {
                        	if(trenutna_stanja.get(j).equals("#")) {
                        		trenutna_stanja.remove(j);
                        	}
                        }
                        //}
                        
                        for (int i = 0; i < trenutna_stanja.size(); i++) {
                        	
                        	
                        		
                        		if(i == 0) {
                        			proslo_stanje = trenutna_stanja.get(i);
                        			System.out.print(trenutna_stanja.get(i));
                        		}
                        	
                        		if(proslo_stanje != trenutna_stanja.get(i)) {
                            	
                        			System.out.print(",");
                        			System.out.print(trenutna_stanja.get(i));
                            	
                            	
                            	
                            		}
                            proslo_stanje = trenutna_stanja.get(i);
                        		
                        
                        }
                       // System.out.println();
                        
                    }

                }

                //znak za novi red
                System.out.println();

            }
        }
    }

    //sto ako ima dva epsilon simbola za redom (boolean nema_promjene)
    private static List<String> epsilon_okruzenje(List<String> trenutna_stanja, HashMap<String, HashMap<String, List<String>>> prijelazi) {

        List<String> rezultat = new ArrayList<String>();
        rezultat.addAll(trenutna_stanja);
        boolean novi_stanja_dodana = true;
        while (novi_stanja_dodana) {
            novi_stanja_dodana = false;
            for (int i = 0; i < rezultat.size(); i++) {
                String trenutno_stanje = rezultat.get(i);

                if (prijelazi.containsKey(trenutno_stanje) &&
                        prijelazi.get(trenutno_stanje).containsKey("$")) {
                    List<String> nova_stanja = prijelazi.get(trenutno_stanje).get("$");
                    for (String novo : nova_stanja) {
                        if (!rezultat.contains(novo)) {
                            rezultat.add(novo);
                            novi_stanja_dodana = true;
                        }
                    }
                }
            }
        }
        return rezultat;
    }


    private static List<String> prijelaz_fja(List<String> trenutna_stanja,
                                             HashMap<String, HashMap<String, List<String>>> prijelazi, String sim) {

        List<String> prosao = new ArrayList<String>();


        for(String sada : trenutna_stanja) {


            if(prijelazi.containsKey(sada) &&
                    prijelazi.get(sada).containsKey(sim) &&
                    !prijelazi.get(sada).get(sim).get(0).equals("#")) {
                //prosao.addAll(prijelazi.get(sada).get(sim));
            	
            	for(String simbol : prijelazi.get(sada).get(sim)) {
            		if(!prosao.contains(simbol)) {
            			prosao.add(simbol);
            		}
            	}
            }
            else {}
        }

        return prosao;
    }


}
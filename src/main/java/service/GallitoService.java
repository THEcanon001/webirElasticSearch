package service;


import com.google.gson.internal.$Gson$Preconditions;
import com.mashape.unirest.http.exceptions.UnirestException;
import entity.Vehicle;

import java.util.ArrayList;

import org.apache.commons.codec.language.bm.Rule;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.UUID;

public final class GallitoService {



    private GallitoService() {
    }

    public static void loadVehicles() throws IOException, JSONException, UnirestException {

        ArrayList<String> ids = getIds();
        ArrayList<Vehicle> vehicles = getVehicles(ids);
        ElasticSearchService.insert(vehicles);



    }




    private static ArrayList<String> getIds() {
        int cantpag = 4;
        int i = 1;
        ArrayList<String> ids = new ArrayList<String>();

        for(i=1; i<= cantpag*40 ; i++)
        {
            String uniqueID = UUID.randomUUID().toString();
            ids.add(uniqueID);


        }
        


        return ids;

    }




    private static ArrayList<Vehicle> getVehicles(ArrayList<String> ids) {
        ArrayList<String> nom_vehiculos = new ArrayList<String>();
        ArrayList<Vehicle> ret = new ArrayList<Vehicle>();
        try {
            //Get Document object after parsing the html from given url.
            Document document;
            String s;
            String s2;
            int i = 0;
            int j = 1;
            int cantpag = 4;



            for(j=1; j<= cantpag; j++) {
                i=(j-1)*40;
                document = Jsoup.connect("https://www.gallito.com.uy/autos/automoviles?pag="+Integer.toString(j)).timeout(0).get();
                Elements ele = document.select("p b");
                Elements ele2 = document.select("p span");
                Elements ele5 = document.getElementsByClass("contenedor-info");
                Elements precios = ele5.select("strong");
                Elements ele6 = document.getElementsByClass("img-responsive aviso-ico-contiene");
                Elements moneda =  precios.select("span");
                Elements ele7 =  document.getElementsByClass("img-seva img-responsive");



                for (Element element : ele7) {
                    Vehicle ve = new Vehicle();
                    ret.add(i,ve);
                    String h = element.toString();
                    String[] cortardiv = h.split("src=");
                    String part1 = cortardiv[0];
                    String part2 = cortardiv[1];
                    String[] src = part2.split("alt");
                    String linkfoto = src[0];
                    String linkgallito= linkfoto.substring(1,linkfoto.length()-2);
                    ve.setPhotos(linkgallito);
                    ve.setId(ids.get(i));
                    i++;
                }



                i=(j-1)*40;
                for (Element element : precios) {
                    String prec =precios.toString();
                    String[] precionum = prec.split("</span>");
                    String precioreal = precionum[1];
                    String[] pre = precioreal.split("</");
                    String prereal = pre[0].replace(".", "");
                    Vehicle ve = ret.get(i);
                    ve.setPrice(Integer.parseInt(prereal));
                    ret.remove(i);
                    ret.add(i,ve);
                    i++;

                }







                i=(j-1)*40;
                for (Element element : ele) {

                    String id = ids.get(i);
                    System.out.println(element.ownText());
                    s = (String) element.text();
                    String[] parts = s.split(" ");
                    Vehicle ve = ret.get(i);
                    ve.setTitle(s);
                    ve.setBrand(parts[0]);
                    ve.setCurrency("U$S");
                    ret.remove(i);
                    ret.add(i,ve);
                    i++;
                }

                i=(j-1)*40;
                for(Element element2: ele2)
                {
                    s = (String) element2.text();
                    String[] parts = s.split(" | ");
                    String part1 = parts[0];
                    String part2 = parts[3];
                    Vehicle ve = ret.get(i);
                    String kilom = part1.replace(".", "");
                    int km = Integer.parseInt(kilom);
                    int year = Integer.parseInt(part2);
                    ve.setKms(km);
                    ve.setYear(year);
                    if (km == 0) {
                        ve.setCondition("New");
                    }
                    else {
                        ve.setCondition("Used");
                    }

                    ret.remove(i);
                    ret.add(i,ve);
                    i++;


                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }










        return ret;

    }

}

//ElasticSearchService.insert(vehicles);

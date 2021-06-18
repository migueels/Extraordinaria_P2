package com.disVaadin.Practica2Ext;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Route
public class MainView extends VerticalLayout {

    //Esta va aser la clase donde desarrollaremos la estructura de grid, modal, botones. etc

    //DECLARMAO LAS VARIABLES

    private final ISuperheroe heroeback;

    private final Button newBoton;
    private final Button export;
    final TextField filtros;

    final Grid<Superheroe> grid;

    //constructor
    public MainView(ISuperheroe heroeback, IBatallas bat, IHabilidades hab){
        this.heroeback = heroeback;
        this.grid = new Grid<>(Superheroe.class);
        this.filtros=new TextField();
        //Los segundos parametros de VaadinIcon seran unos ClickLitener
        this.newBoton = new Button("Nuevo Heroe", VaadinIcon.PLUS.create());
        this.export = new Button("Guardar Heroe", VaadinIcon.ARROW_CIRCLE_RIGHT.create());

        //INICIO DEL LAYOUT VAADIN

        HorizontalLayout layout = new HorizontalLayout(newBoton,export,filtros);
        //layout.addComponents(newBoton,export,filtros);
        //layout.addComponent(grid);
        add(layout,grid);
        grid.setHeight("400px");//una altura de 400 px para el grid que vamos a usar


        //el identificador de cada heroes no lo mostramos ya que solo nos hara falta para referenciarlo
        grid.removeColumnByKey("id");
        //nos piden mostrar la informacion relaccionada con los superheroes de : Nombre, Identidad, Procedecia, genero, Numeor de Batllas y habilidades
        //vamos a crear el grid con esos objetos del json
        grid.setColumns("nombre","identidadSecreta","procedencia","genero","numeroBatallas","numeroHabilidades");
        //Al igual que en la primera practica, buscamos los superheroes por su nombre
        //nombre, que no la identidad secreta
        filtros.setPlaceholder("Busqueda por Nombre: ");

        grid.asSingleSelect().addValueChangeListener(e -> {
            //necesitamos una funcion modal par apasale la pelicula seleciconada
            //necesitaremos heroeback, hab y bat del contructor
            modalinfo(hab,bat,e.getValue(),heroeback);

        });

        //actualizacion
        //filtros.setValueChangeMode(ValueChangeMode.EAGER);
        //filtros.addValueChangeListener(e -> listCustomers(e.getValue()));
        filtros.setValueChangeMode(ValueChangeMode.EAGER);
        filtros.addValueChangeListener(e -> listCustomers(e.getValue()));

        //meter funcion NuevoHeroeModal
        newBoton.addClickListener(e -> nuevoHeroeModal(bat,hab,heroeback));


        export.addClickListener(e ->
                {
                    try {
                        //guardamos en el json
                        guardadoJson(bat, hab, heroeback); //crear funcion para guardar en el json
                        // notificacion, para avisar al usuario que se ha exportado correctamente
                        Notification.show("Datos guardados correctamente en el json utilizado");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
        );

        // inicalizamos la lista
        listCustomers(null);

        //FIN DEL LAYOUT VADDIN
    }

    private void modalinfo(IHabilidades hab, IBatallas bat, Superheroe s, ISuperheroe heroeback) {
        try{
            Dialog dialog = new Dialog(); //modal instanciado
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);

            //ponemos en el layout los campos a mostrar
            dialog.add(new HorizontalLayout(new Html("<b>Nombre: </b>"), new Text(s.getNombre())));
            dialog.add(new HorizontalLayout(new Html("<b>Identidad Secreta: </b>"), new Text(s.getIdentidadSecreta())));
            dialog.add(new HorizontalLayout(new Html("<b>Genero: </b>"), new Text(s.getGenero())));
            dialog.add(new HorizontalLayout(new Html("<b>Procedencia: </b>"), new Text(s.getProcedencia())));

            //tenemos en cuenta los arrays de batallas y de habilidades para poder mostrarlos
            //BATALLAS
            for(Batalla b : bat.findByidSuperheroe(s.getId())){
                //recorremos las battallas para el identificador de un superheroe
                //dialog.add(new HorizontalLayout(new Html("<b>Fecha Inicio: </b>"), new Text(b.getFecha_inicio())));
                //dialog.add(new HorizontalLayout(new Html("<b>Fecha Fin: </b>"), new Text(b.getFecha_fin())));
                dialog.add(new HorizontalLayout(new Html("<b>Lugar: </b>"), new Text(b.getLugar())));
                ;
            }

            //habilidades
            for (Habilidad h : hab.findByidSuperheroe(s.getId())){
                dialog.add(new HorizontalLayout(new Html("<b>Tipo: </b>"), new Text(h.getTipo())));
                dialog.add(new HorizontalLayout(new Html("<b>Descripcion: </b>"), new Text(h.getDefinicion())));
            }

            //Añadimos los botones al layout
            Button confirmButton = new Button("Editar", event -> { dialog.close(); editarModal(hab,bat,s,heroeback); });
            Button cancelButton = new Button("Cancelar", event -> { dialog.close(); });
            HorizontalLayout actions2 = new HorizontalLayout(confirmButton, cancelButton);
            dialog.add(actions2);
            //abrimos el modal
            dialog.open();

        }catch (Exception e){

        }
    }



    void guardadoJson(IBatallas bat, IHabilidades hab, ISuperheroe heroeback) throws IOException{
        //vamos a guardar / actualizar los nuevos datos de los superheroes en el json
        String dir = "{\"Heroe\": {\"Superheroes\": { \"Superheroe\": [";
        //String msg = "{\"Success\":true,\"Message\":\"Invalid access token.\"}";
        //necesitamos los superheroes del json
        List<Superheroe> superheroe = heroeback.findAll();
        for(int i = 0; i< superheroe.size(); i++){
            //recorremos con un for el tamalo del array de superheroes
            Long idSuperheroe = superheroe.get(i).getId(); //cogemos el identificador generado automaticamente
            String nombre = superheroe.get(i).getNombre(); //cogemos el nombre y lo asignamos a variable
            String identidadS = superheroe.get(i).getIdentidadSecreta(); //lo mismo con la identidad secreta
            String genero = superheroe.get(i).getGenero(); //lo mismo con el genero
            String procedencia = superheroe.get(i).getProcedencia(); // lo mismo con la procedencia
            int numHabilidades = superheroe.get(i).getNumeroHabilidades();
            int numBatallas = superheroe.get(i).getNumeroBatallas(); //cogemos el numero de batallas
            //para el array de batallas y habilidades lo hacemos de forma diferente
            dir = dir + "{\"Nombre\":\""+nombre+ "\",\"IdentidadS\":\""+identidadS+ "\",\"procedencia\":\""+procedencia+"\",\"genero\":\""+genero+"\",";
            //Tras la uta van las habilidades
            //evaluamos si hay Hbailidades o no (numero habilidades == 0)
            if(numHabilidades == 0){
                //si no tenemos habilidades
                dir = dir.substring(0,dir.length()-1);

            }else{
                //en caso de tener varios, vamos cogiendo rutas y cerrando
                //ojo al cerrar por que despues van las batallas
                //buscamos las habiliades por el identificador del superheroe
                List<Habilidad> habilities = hab.findByidSuperheroe(idSuperheroe);
                dir = dir + "\"Habilidades\": {\"Habilidad\": [";
                for(int k = 0; k<habilities.size();k++){
                    //recorremos las habilidades
                    String tipo = habilities.get(k).getTipo(); //cogemos el tipo de las habilidades
                    String defi = habilities.get(k).getDefinicion(); //cogemos la definciion de la habilidad
                    dir+="{\"Tipo\":\""+tipo+ "\",\"Definicion\":\""+defi+ "\"},";

                }
                dir =dir.substring(0, dir.length()-1);
                dir+="]";
            }

            dir = dir + "},";

            //vemos si el numero de batllas y habilidades es 0 o contiene informacion
            if (numBatallas == 0){
                //en caso de que no tengamos batallas
                //eliminamos la ultima columna al no tener
                dir = dir.substring(0,dir.length()-1);
            }else{
                //si tenemos batallas...
                //buscamos el superheroe al que pertence con el identificador del mismo
                List<Batalla> wars = bat.findByidSuperheroe(idSuperheroe);
                dir = dir + "\"id\": \""+idSuperheroe+" \",\"Batallas\":{\"Batalla\":[";
                for(int j =0; j<wars.size(); j++){
                    String finicio = wars.get(j).getFecha_inicio();
                    String ffinal = wars.get(j).getFecha_fin();
                    String lugar = wars.get(j).getLugar();
                    dir+="{\"Fecha_inicio\":\""+finicio+ "\",\"Fecha_fin\":\""+ffinal+ "\",\"Lugar\": \""+lugar+"\" },";
                }

                dir = dir.substring(0, dir.length()-1);
                dir+="]}"; //cerrramos la ruta de las batallas
            }
            dir+="},"; //dejamos la posibilidad de econtrarnos mas habilidades

        }
        dir = dir.substring(0,dir.length()-1);

        dir+="]}}}"; //el cierre por si no hay mas y acab el json
        Gson gson3 = new Gson();
        JsonElement jelem2;
        jelem2 = gson3.fromJson(dir, JsonElement.class);
        JsonObject jobj2 = jelem2.getAsJsonObject();

        try (Writer writer2 = new FileWriter("Superhereoes_JSON.json")) {
            Gson gson4 = new GsonBuilder().create();
            gson4.toJson(jobj2, writer2);
        }




    }



    void nuevoHeroeModal(IBatallas bat, IHabilidades hab, ISuperheroe heroeback) {
        //nuevos superheroes
        Dialog dialog = new Dialog();
        TextField nombre = new TextField("Nombre");
        dialog.add(new HorizontalLayout(nombre));
        TextField identidad = new TextField("Identidad Secreta: ");
        dialog.add(new HorizontalLayout(identidad));
        TextField procedencia = new TextField("Procedencia: ");
        dialog.add(new HorizontalLayout(procedencia));
        TextField genero = new TextField("Genero");
        dialog.add(new HorizontalLayout(genero));

        //Tendremos un textfield donde el usuario podrá indicar cuantas habilidades tiene el superhereo y en cuantas batallas participa
        //de esta forma en funcion de la cantidad que ponga, se abriran tantos modales para rellenar

        //usamos para ello NumberField
        //NumberField para habilidades
        NumberField numeroHabilidades = new NumberField("Numero de habilidades");
        dialog.add(new HorizontalLayout(numeroHabilidades));

        //NumberField para batallas
        NumberField numeroBatallas = new NumberField("Numero de batallas");
        dialog.add(new HorizontalLayout(numeroBatallas));

        //cramos un boton de aceptar que será el que genere los modales nuevos
        Button acept = new Button("Aceptar", evento -> {
            Double nbat = numeroBatallas.getValue();
            Double nhab = numeroHabilidades.getValue();
            int nbatallas = 0;
            int nhabilidades = 0;
            if(nbat != null){
                nbatallas = nbat.intValue();
            }
            if (nhab != null){
                nhabilidades = nhab.intValue();
            }
            //una vez introducidos los datos segun la cantidad de batallas y habilidades,
            //registramos el nuevo superheroe

            Superheroe newHeroe = new Superheroe(nombre.getValue(),identidad.getValue(),procedencia.getValue(),genero.getValue(),nbatallas,nhabilidades);


            heroeback.save(newHeroe); //guardamos el nuevo heroe añadido
            //teniendolo añadido habra que actualizar para que a la proxima que se tengan que mostrar, slgan los nuevos tambien
            listCustomers("");
            dialog.close(); //cerramos

            //Cuando el usuario introduce la cantidad de batallas y habilidades, saldran tanto modales como se indiques
            //comprobamos el numeor
            if(nbatallas>0){
                //lamo a la funcion de crear el modal para introucir las batallas
                //desarrollada mas abajo
                nuevasBatallasModal(bat,nbatallas,newHeroe.getId());
            }
            //lo mismo para las habilidades
            if(nhabilidades>0){
                nuevasHabilidadesModal(hab,nhabilidades,newHeroe.getId());
            }

        });

        //Creamos el boton de cancelar para poder cancelar el porceso de añadir nuevos
        Button cancelar = new Button("Cancelar", e -> {
            dialog.close();
        });
        //añadimos los botones creados al horizontal layout
        HorizontalLayout options = new HorizontalLayout(acept,cancelar);
        dialog.add(options);
        dialog.open();

    }

    void nuevasBatallasModal(IBatallas bat, int nbatallas, Long id) {
        //MODAL PARA AÑADIR NUEVAS HAABILIDADES
        Dialog dialog = new Dialog(); //creaciuon del modal para añadir los nuevas batallas
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        //usamos array para cada uno de los compoenentes, ya que podemos tener mas de una batalla
        //usaremos text field
        TextField fechaInicio[] = new TextField[nbatallas]; //aray para el tipo de habilidades
        TextField fechaFin[] = new TextField[nbatallas]; //un array de descripcion para la cantidad de habilidades
        TextField lugar[] = new TextField[nbatallas]; //array con los lugares de la batalla

        //Especificamos los campos que deberá de rellenar
        //recorremos el numero de habilidades y aladirmos los campos al layout
        for(int i = 0; i<nbatallas; i++){
            fechaInicio[i] = new TextField("Fecha inicio batalla");
            dialog.add(new HorizontalLayout(fechaInicio[i])); //añadirmos al layoit el campo
            fechaFin[i] = new TextField("Fecha fin batalla ");
            dialog.add(new HorizontalLayout(fechaFin[i])); //añadimoa la decripcion al layout
            lugar[i] = new TextField("Lugar ");
            dialog.add(new HorizontalLayout(lugar[i]));


        }

        //creamos un nuevo boton para poder añadir las batallas declaradas
        //usamos un new button y lo añadiremos al layout
        Button accept = new Button("Aceptar", e -> {
            for(int i = 0; i<nbatallas;i++){
                Batalla newBat = new Batalla(fechaInicio[i].getValue(),fechaFin[i].getValue(),lugar[i].getValue(),id);
                //gaurdamos los cambios
                bat.save(newBat);
                //cerramos el modal una vez pulsado el boton de aceptar

                dialog.close();
            }
        });
        //creamos el otro boton que es el de cancelar
        Button denegar = new Button("Abortar", e -> {
            dialog.close();
        });
        HorizontalLayout options = new HorizontalLayout(accept, denegar);
        dialog.add(options);
        dialog.open();


    }
    /*void nuevasBatallasModal(IBatallas bat, int nbatallas, Long id) {
        //MODAL PARA AÑADIR NUEVAS BATALLAS
        //MODAL PARA AÑADIR NUEVAS BATALLAS
        //hacemos un bucle en el que crearemos un modal para cada batalla
        for (int i=0; i < nbatallas; i++) {

            // declaramos el modal
            Dialog dialog = new Dialog();
            TextField lugar = new TextField("Lugar: ");
            dialog.add(new HorizontalLayout(lugar));
            TextField fecha_inicio = new TextField("Fecha de inicio:");
            dialog.add(new HorizontalLayout(fecha_inicio));
            TextField fecha_fin = new TextField("Fecha de fin: ");
            dialog.add(new HorizontalLayout(fecha_fin));
            //TextField id_superheroe = new TextField("Id del superheroe que participa: ");
           // dialog.add(new HorizontalLayout(id_superheroe));

            Button confirmButton = new Button("Aceptar", event -> {

                //inicializamos la batalla nueva con los valores del modal
                Batalla nuevo = new Batalla(lugar.getValue(), fecha_inicio.getValue(), fecha_fin.getValue(), id);
                bat.save(nuevo); //guardamos el modal
                listCustomers("");
                dialog.close();
            });

            Button cancelButton = new Button("Cancelar", event -> {co
                dialog.close();
            });
            HorizontalLayout actions2 = new HorizontalLayout(confirmButton, cancelButton);
            dialog.add(actions2);
            //abrimos el modal
            dialog.open();
        }
    }*/

    void nuevasHabilidadesModal(IHabilidades hab, int nhabilidades, Long id){
        //MODAL PARA AÑADIR NUEVAS HAABILIDADES
        Dialog dialog = new Dialog(); //creaciuon del modal para añadir los nuevas batallas
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        //usamos array para cada uno de los compoenentes, ya que podemos tener mas de una batalla
        //usaremos text field
        TextField tipo[] = new TextField[nhabilidades]; //aray para el tipo de habilidades
        TextField descripcion[] = new TextField[nhabilidades]; //un array de descripcion para la cantidad de habilidades

        //Especificamos los campos que deberá de rellenar
        //recorremos el numero de habilidades y aladirmos los campos al layout
        for(int i = 0; i<nhabilidades; i++){
            tipo[i] = new TextField("Tipo habilidad");
            dialog.add(new HorizontalLayout(tipo[i])); //añadirmos al layoit el campo
            descripcion[i] = new TextField("Descripcion ");
            dialog.add(new HorizontalLayout(descripcion[i])); //añadimoa la decripcion al layout

        }

        //creamos un nuevo boton para poder añadir las batallas declaradas
        //usamos un new button y lo añadiremos al layout
        Button accept = new Button("Aceptar", e -> {
            for(int i = 0; i<nhabilidades;i++){
                Habilidad newHab = new Habilidad(tipo[i].getValue(),descripcion[i].getValue(),id);
                //gaurdamos los cambios
                hab.save(newHab);
                //cerramos el modal una vez pulsado el boton de aceptar

                dialog.close();
            }
        });
         //creamos el otro boton que es el de cancelar
        Button denegar = new Button("Abortar", e -> {
            dialog.close();
        });
        HorizontalLayout options = new HorizontalLayout(accept, denegar);
        dialog.add(options);
        dialog.open();
    }

    private void editarModal(IHabilidades hab, IBatallas bat, Superheroe s, ISuperheroe heroeback) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        //Declaramos los campos que van a aparecer para editar el modal
        //lo campos son los declrados anteriormente
        //usamos TextField
        TextField nombre = new TextField("Nombre");
        nombre.setValue(s.getNombre());
        dialog.add(new HorizontalLayout(nombre));
        TextField identidads = new TextField("Identidad Secreta");
        identidads.setValue(s.getIdentidadSecreta());
        dialog.add(new HorizontalLayout(identidads));
        TextField genero = new TextField("Genero");
        genero.setValue(s.getGenero());
        dialog.add(new HorizontalLayout(genero));
        TextField procedencia = new TextField("Procedencia");
        procedencia.setValue(s.getProcedencia());
        dialog.add(new HorizontalLayout(procedencia));

        int numeroBatallas = s.getNumeroBatallas();
        int numeroHabilidades = s.getNumeroHabilidades();
        Habilidad h[] = new Habilidad[numeroHabilidades];
        Batalla b[] = new Batalla[numeroBatallas];

        //empezamos con las habilidades
        TextField tipohab[]= new TextField[numeroHabilidades];
        TextField defihab[]= new TextField[numeroHabilidades];
        int i =0;

        //recorremos el total de las habilidades
        for(Habilidad habilities : hab.findByidSuperheroe(s.getId())){
            h[i] = habilities;
            tipohab[i] = new TextField("Tipo de habilidad");
            tipohab[i].setValue(habilities.getTipo());
            dialog.add(new HorizontalLayout(tipohab[i]));

            defihab[i] = new TextField("Definicion de habilidad");
            defihab[i].setValue(habilities.getTipo());
            dialog.add(new HorizontalLayout(defihab[i]));
            i++;

        }
        int j=0;

        //recorremos el total de las batllas
        //empezamos con las habilidades
        TextField fechini[]= new TextField[numeroBatallas];
        TextField fechfin[]= new TextField[numeroBatallas];
        TextField lugar[]= new TextField[numeroBatallas];
        for(Batalla wars : bat.findByidSuperheroe(s.getId())){
            b[j] = wars;
            fechini[j] = new TextField("Fecha de inicio");
            fechini[j].setValue(wars.getFecha_inicio());
            dialog.add(new HorizontalLayout(fechini[j]));

            fechfin[j] = new TextField("Fecha fin");
            fechfin[j].setValue(wars.getFecha_fin());
            dialog.add(new HorizontalLayout(fechfin[j]));

            lugar[j] = new TextField("Lugar fin");
            lugar[j].setValue(wars.getLugar());
            dialog.add(new HorizontalLayout(lugar[j]));
            j++;

        }

        //cremos el boton de aceptar que sera el que pueda cambiar los superheroes
        Button editar = new Button("Confirmar", e -> {
            s.setNombre(nombre.getValue());
            s.setIdentidadSecreta(identidads.getValue());
            s.setGenero(genero.getValue());
            s.setProcedencia(procedencia.getValue());
            //una ve metidos estos campos pasamos a recorrer los arrays y a añadirlos
            for(int k = 0; k<numeroHabilidades;k++){
                h[k].setTipo(tipohab[k].getValue());
                h[k].setDefinicion(defihab[k].getValue());
                hab.save(h[k]);

            }
            //bucel for para las batallas
            for(int m = 0; m<numeroBatallas;m++){
                b[m].setFecha_inicio(fechini[m].getValue());
                b[m].setFecha_fin(fechfin[m].getValue());
                b[m].setLugar(lugar[m].getValue());
                bat.save(b[m]);

            }

            //guardaos los cambios en suoerheroes
            heroeback.save(s); //guardado el cambio en el superheroe
            listCustomers("");
            dialog.close();


        });

        //cremoa un boton de abortar o cancelar y los añadirmos al layout
        Button cancelar = new Button("Cancelar", event -> { dialog.close(); });
        HorizontalLayout opt = new HorizontalLayout(editar, cancelar);
        dialog.add(opt);
        //abrimos el modal
        dialog.open();

    }

    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)){
            grid.setItems(heroeback.findAll());
        }
        else grid.setItems(heroeback.findByNombreStartsWithIgnoreCase(filterText));
    }


    //METODOS A USAR

}

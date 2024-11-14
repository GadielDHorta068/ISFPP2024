# ISFPP2024 
## Instancia Supervisada de Formación Práctica Profesional (ISFPP)

Si estas leyendo esto, probablemente no deberias estar aca
## Instrucciones de uso:
###  [Último Release](https://github.com/GadielDHorta068/ISFPP2024/releases/latest)
### [Documentación](https://github.com/GadielDHorta068/ISFPP2024/wiki/Alcanze-del-proyecto)
### [Documentación Javadoc](https://gadieldhorta068.github.io/ISFPP2024/)



Deberas tener instalado Java 21 o superior en tu sistema.
Tambien deberas tene acceso a internet para la gestion de la Base de datos del programa

Se te sera solicitado en el primer inicio del programa, tu nombre y el idioma de preferencia para poder personalizar tu experiencia de usuario
![Seleccion de idiomas al iniciar aplicacion](Aplication/src/main/resources/assets/idioma.png)


En el panel principal encontraras 3 tablas con la información basica de los equipos existentes, las conexiones, y las ubicaciones disponibles.
Al hacer click en alguna, se mostrara en el panel derecho información detallada con las propiedades del objeto seleccionado.
![Panel principal](Aplication/src/main/resources/assets/principal.png)


### Eliminar un objeto:
Deberas hacer click en uno de los objetos, luego dirigirte a la barra superior.
Seleccionar editar y luego eliminar, en caso de un equipo, se eliminara también sus conexiones a otros equipos. Y al querer eliminar una ubicacion se chekeara primero que no hayan equipos en ese sitio
![Eliminacion de un elemento](Aplication/src/main/resources/assets/eliminar.png)


### Modificar un elemento:
Deberas hacer click en el objeto a modificar, luego en la barra superior seleccionaras en "Modificar", lo cual abrira un panel con sus propiedades a cambiar
![Modificar un elemento](Aplication/src/main/resources/assets/editar.png)

### Crear elementos:
En la barra superior, deberas presionar el boton de editar, en el mismo en sus opciones ofrecera crear una conexion, un equipo, una locacion o puertos nuevos

### Editar puertos:
Deberas seleccionar un equipo de la tabla, luego presionar en editar puertos, se desplegara un menu con todos los puertos existentes donde podemos incrementar o decrementar siempre y cuando no se borren puertos en uso
![Edicion de puertos](Aplication/src/main/resources/assets/puertos.png)

### Herramientas:
Esa seccion desplegable de la barra superior, otorgara diferentes utilidades para realizar analisis o visualizacion de datos sobre la LAN
Entre las caules nos encontraremos:
* Ping en rango: Al seleccionar una equipo y luego este item, nos hara un escaneo de todas las direcciones ip bajo esa denominacion
* Equipos Activos: Devolvera una lista con todos los equipos de la LAN que se encuentren activos. True si esta on, False si esta apagado o es inalcanzable
* Conexiones: Todos los equipos alcanzados por el seleccionado
* Ver Grafico: Se nos mostrara visualmente como esta compuesta la LAN
* Traceroute: Nos enseña el camino mas rapido entre dos equipos deseados
* Equipo ON/OFF : Simulara el apagado o encendido de un equipo

![Herramientas](Aplication/src/main/resources/assets/herramientas.png)

### Vistas:
Este boton alternara las vistas entre 
* Equipos   Conexiones  Locaciones 
* Tipo de:    Puerto  Equipo  Cable

De los cuales este ultimo grupo no es eliminable ya que cuando un cable es creado, no es eliminado de la realidad, solo no se hara uso en el armado de la red

## Integrantes del grupo:
|🧑‍💻Gadiel D'Horta 
|👩‍💻Rocio 
|👨‍💻Ceferino

![Lo pibe'](Aplication/src/main/resources/assets/shrek.png)

# [MusicForum]

## 👥 Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| Carlos Moreno Cano | c.morenoc.2022@alumnos.urjc.es | roomtrash6 |
| Lorena López Gallego-Casilda | l.lopezga.2023@alumnos.urjc.es | msbuns01 |
| Óscar Rodríguez Pérez | o.rodriguez.2022@alumnos.urjc.es | OscarRP15 |


---

## 🎭 **Preparación: Definición del Proyecto**

### **Descripción del Tema**
Es una aplicación web tipo foro para que los amantes de la música compartan sus opiniones y debatan con otros usuarios sobre nuevos lanzamientos, sus artistas favoritos, etc... 
El objetivo de la aplicación es crear un ambiente acogedor y respetuoso en el que los usuarios se sientan cómodos escribiendo sobre música.

### **Entidades**
Indicar las entidades principales que gestionará la aplicación y las relaciones entre ellas:

1. **[Entidad 1]**: Usuario
2. **[Entidad 2]**: Post
3. **[Entidad 3]**: Comentario
4. **[Entidad 4]**: Álbum


**Relaciones entre entidades:**
- Usuario - Post: Un usuario puede tener múltiples posts pero la autoría de un post no puede ser de varios usuarios simultáneamente (1:N).
- Usuario - Comentario: Un usuario puede escribir varios comentarios pero un comentario no puede ser de varios usuarios (1:N).
- Post - Comentario: Un post puede contener múltiples comentarios pero un comentario no puede pertenecer a más de un post (1:N).
- Post - Álbum: Un post puede tratar de varios álbumes y un álbum puede estar etiquetado en varios posts (N:M)

### **Permisos de los Usuarios**
Describir los permisos de cada tipo de usuario e indicar de qué entidades es dueño:

* **Usuario Anónimo**: 
  - Permisos: Visualización del contenido de otros usuarios.
  - No es dueño de ninguna entidad

* **Usuario Registrado**: 
  - Permisos: Creación de posts bajo su nombre, creación de comentarios bajo su nombre, valoración del contenido de otros usuarios, edición de su perfil público.
  - Es dueño de: Sus posts y sus comentarios.

* **Administrador**: 
  - Permisos: Tiene permisos de creación, edición, y borrado de todas las entidades de la web. Esto incluye todos los posts y comentarios de todos los usuarios, y todos los títulos de música que se pueden discutir en la página.
  - Es dueño de: Es dueño de todas las entidades de la web.

### **Imágenes**
Indicar qué entidades tendrán asociadas una o varias imágenes:

- **[Entidad con imágenes 1]**: Usuario
- **[Entidad con imágenes 2]**: Álbum
- **[Entidad con imágenes 3]**: Post


---

## 🛠 **Práctica 1: Maquetación de páginas con HTML y CSS**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=M-PBvYJEMNM)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Diagrama de Navegación**
Diagrama que muestra cómo se navega entre las diferentes páginas de la aplicación:

![Diagrama de Navegación](images/Diagrama-navegacion-pantallas.jpg)

> [Descripción opcional del flujo de navegación: Ej: "El usuario puede acceder desde la página principal a todas las secciones mediante el menú de navegación. Los usuarios anónimos solo tienen acceso a las páginas públicas, mientras que los registrados pueden acceder a su perfil y panel de usuario."]

### **Capturas de Pantalla y Descripción de Páginas**

#### **1. Página Principal / Inicio**
![Página Principal](images/MusicForum_Inicio.png)

> La página principal muestra los post más destacados del foro e incluye un botón para acceder al resto de los posts. Incluye barra de navegación, la cual da acceso a: inicio, álbumes, registro de usuario e inicio de sesión. Nota: esta barra de navegación está presente en todas las páginas.

#### **2. Página Posts Publicados/ Listado de Posts**
![Página Posts Publicados](images/MusicForum-Listado-de-Posts.png)
> Esta página muestra un listado de los post publicados. Incluye un botón para ver el post completo y sus comentarios.

#### **3. Página Ver Post / Post**
![Página Ver Post](images/MusicForum-Post.png)
>Esta página es una página genérica para todos los posts, la cual incluye: el post completo, los comentarios ya publicados, un botón para volver a la página de posts publicados y un formulario para dejar una opinión.

#### **4. Página Álbumes/ Listado de Álbumes**
![Página Ábumes](images/MusicForum-Listado-de-Albumes.png)
> Esta página muestra una lista de los álbumes disponibles en el foro. Se incluye un botón para ver el listado de canciones de cada álbum.

#### **5. Página Lista Canciones Álbum / Álbum**
![Página Ver Posts](images/MusicForum-Album.png)
> Esta página muestra las canciones que componen el albúm seleccionado, además de la portada y un botón para volver a la lista de álbumes.

#### **6. Página de Login / Login**
![Página de Login](images/MusicForum-Login.png)
> Esta página muestra un formulario para que cada usuario inicie sesión en su cuenta. En caso de no estar regstrado, se incluye un botón que le redirijirá a la página de registro. Como todavía no está implementada una funcionalidad real, se ha incluido en la barra de navegación un acceso a la página "Mi Perfil" para simular que, solo aquel usuario registrado, tiene acceso a esta.

#### **7. Página de Registro/ Registro**
![Página de Resgistro](images/MusicForum-Registro.png)
> Esta página muestra un formulario para que cada usuario cree una cuenta en el foro. En caso de ya tener cuenta, se incluye un botó que le redirijirá a la página de login.

#### **8. Página Perfil de Usuario / Mi perfil**
![Página Perfil de Usuario](images/MusicForum-Mi-Perfil.png)
> Esta página incluye la información del perfil de un usuario (nombre, apellidos, nombre de usuario, miembro desde:, avatar). Se incluyen botones para ver los post propios del usuario, publicar un post y editar el perfil.

#### **9. Página Ver Mis Posts / Mis Post**
![Página Ver Mis Posts](images/MusicForum-Mis-posts.png)
> Esta página incluye un lista de los post que ha publicado el usuario registrado. En cada post aparecen dos botones, uno para editar el post y otro para eliminarlo. También muestra un botón para volver a la página del perfil.

#### **10. Página Ver Todos Los Usuarios / Listado de usuarios**
![Página Ver Todos Los Usuarios](images/Listado-de-users.png)
> Esta página sólo es accesible para el administrador de la página web. Desde ahí podrá ver los usuarios, editar sus perfiles, o borrarlos.

#### **11. Página Panel de Administrador / Admin Panel**
![Página Panel de Administrador](images/MusicForum-Panel-de-Administrador.png)
> Esta página sólo es accesible para el administrador de la página web. Desde ahí tendrá acceso a la página de consultar la lista de usuarios pudiendo editar según considere, ver los posts de la página web para editar según considere, y podrá ver los álbumes registrados en la base de datos de la web, desde donde podrá potencialmente borrar o crear entradas nuevas.

#### **12. Página Editar Perfil / Editar Perfil**
![Página Editar Perfil](images/MusicForum-Editar-perfil.png)
> Formulario para editar un perfil. Accesible para usuarios registrados para aplicarse en los mismos y para el administrador para aplicarlo en cualquier usuario.

#### **13. Página Editar Post / Editar Post**
![Página Editar Post](images/MusicForum-Editar-post.png)
> Formulario para editar un post. Accesible para el admin para todos los posts o para un usuario para sus posts.

#### **14. Página Listado de Álbumes Admin View/ Listado de Álbumes Admin View**
![Página Listado de Álbumes](images/MusicForum-Listado-de-Albumes-Admin-View.png)
> Esta página muestra los álbumes registrados en la página con la opción extra de añadir un álbum nuevo.

#### **15. Página Ver Posts Admin View/ Ver Posts Admin View**
![Página Ver Posts Admin View](images/MusicForum-Listado-de-Posts-Admin-View.png)
> Esta página muestra los posts subidos a MusicForum. Se diferencia en el listado de usuarios anterior en que se tiene un botón para borrar los posts.


### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - [Carlos Moreno Cano]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [base spring boot project](https://github.com/DWS-2026/dws-2026-project-base/commit/6349ae93126f11e252194e108f988a737a92d18b)  | [backend/pom.xml](https://github.com/DWS-2026/dws-2026-project-base/commit/6349ae93126f11e252194e108f988a737a92d18b#diff-34049c3bc6deee4bbf269544338e0450399140da63fec684096d1ae0ce70b4bb)   |
|2| [Re-edit de un par de cosas del README.md que no se guardaron y adición de un html para listar los álbumes de la página.](https://github.com/DWS-2026/dws-2026-project-base/commit/8013174948aa74f31f7c35f91c6f4d8805d68c0)  | [frontend/album_listing.html](https://github.com/DWS-2026/dws-2026-project-base/commit/8013174948aa74f31f7c35f91c6f4d8805d68c07#diff-e351d6d4fb741c2abf7cfb206875171f1f882a578a8a2ebb668b8d670b438446)   |
|3| [new html file for post_listing and updated reference in the welcome page to it](https://github.com/DWS-2026/dws-2026-project-base/commit/a11e8d127f5ddbe71ca18388745fcb860973484d)  | [frontend/post_listing.html](https://github.com/DWS-2026/dws-2026-project-base/commit/a11e8d127f5ddbe71ca18388745fcb860973484d#diff-75e79d7dff23bd46c556b615c829d020f296b2239509e354df494a482ede3afc)   |
|4| [added admin_panel and some new pages. Those are still essentially empty: they only have the footer and the navbar. Will finish all this after lunch](https://github.com/DWS-2026/dws-2026-project-base/commit/8e9ad4d322605c8eb5fbd995c91cea52526e8d3f)  | [frontend/admin_panel.html](https://github.com/DWS-2026/dws-2026-project-base/commit/8e9ad4d322605c8eb5fbd995c91cea52526e8d3f#diff-a99ee44ee16ae8644114c00056148b725cc57c686ba3df37a838b381a0ce963d)   |
|5| [deleted a form because it wasn't needed. Improved the admin panel and user listing now works.](https://github.com/DWS-2026/dws-2026-project-base/commit/6b1d0763dd414e6269a978af40748fb689a2b578)  | [frontend/user_listing.html](https://github.com/DWS-2026/dws-2026-project-base/commit/6b1d0763dd414e6269a978af40748fb689a2b578#diff-67c12214d4fb8a3e0aafc2e40f622ab40600c9fa21e36f0f3881b77bae0ae67a)   |

---

---

#### **Alumno 2 - [Lorena López Gallego Casilda]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Creación de la primera página de la web](https://github.com/DWS-2026/project-grupo-2/commit/882d7339dc0055b355ea5fe2004fc1e4f01ab56d)  | [first.html](frontend/first.html)   |
|2| [Creación página de registro y de login](https://github.com/DWS-2026/project-grupo-2/commit/525707113f232cb4544a3fc7ab17b779723df0c4)  | [register.html](frontend/register.html) [login.html](frontend/login.html)   |
|3| [Creación de las páginas de perfil de usuario y edición de ese perfil](https://github.com/DWS-2026/project-grupo-2/commit/0696a402720caaa5efa3d8651bcf25927291b5c4)  | [user_profile.html](frontend/user_profile.html) [edit_profile.html](frontend/edit_profile.html)   |
|4| [Posibilidad de "editar" los posts y de ver mis posts como usuario](https://github.com/DWS-2026/project-grupo-2/commit/5a633160d2c3ea7373aa83a84cfe078ed47f449f)  | [edit_post.html](frontend/edit_post.html) [user_posts.html](frontend/user_posts.html)   |
|5| [En las páginas de listado de posts y álbumes añadir botones especiales para el admin](https://github.com/DWS-2026/project-grupo-2/commit/50b478c1ed8a024232175f4b3b07ec61e704e09c)  | [album_listing.html](frontend/user_posts.html) [post_listing.html](frontend/post_listing.html)   |

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    |                                                                     Commits                                                                      |                                                                                                                                                                                        Files                                                                                                                                                                                         |
|:------------: |:------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|1|   [Creacion de vista genérica de album y de post](https://github.com/DWS-2026/project-grupo-2/commit/645338a7587c97da2c3f703187d66eebf146de70)   |    [album__view.html](https://github.com/DWS-2026/project-grupo-2/commit/645338a7587c97da2c3f703187d66eebf146de70#diff-48a3c797f16e91a20a8c2c5905b0e40f73126f5d75e3c0f6f186584791355de6)<br/>[post_view.html](https://github.com/DWS-2026/project-grupo-2/commit/645338a7587c97da2c3f703187d66eebf146de70#diff-c8ed244d96c172dc2fbc8b6d4e8026b019f9dfee10cd0dd0947c579bd908eefc)     |
|2| [Creacion botones para ir a vista de album y post ](https://github.com/DWS-2026/project-grupo-2/commit/360297ac762c8e7fd750cf70dd6515e690ad54b6) | [album_listing.html](https://github.com/DWS-2026/project-grupo-2/commit/360297ac762c8e7fd750cf70dd6515e690ad54b6#diff-e351d6d4fb741c2abf7cfb206875171f1f882a578a8a2ebb668b8d670b438446) <br/> [post_listing.html](https://github.com/DWS-2026/project-grupo-2/commit/360297ac762c8e7fd750cf70dd6515e690ad54b6#diff-75e79d7dff23bd46c556b615c829d020f296b2239509e354df494a482ede3afc) |
|3|            [Añadido comentarios en post](https://github.com/DWS-2026/project-grupo-2/commit/1ae8c99430ee915d7273f0af13c9be0665434e62)            |                                                                                                 [post_view.html](https://github.com/DWS-2026/project-grupo-2/commit/1ae8c99430ee915d7273f0af13c9be0665434e62#diff-c8ed244d96c172dc2fbc8b6d4e8026b019f9dfee10cd0dd0947c579bd908eefc)                                                                                                  |
|4|          [Añadido directorios de CSS y JS](https://github.com/DWS-2026/project-grupo-2/commit/523cb33ef13880d972a7aa1c04f531ae2ad21791)          |              [CSS](https://github.com/DWS-2026/project-grupo-2/commit/523cb33ef13880d972a7aa1c04f531ae2ad21791#diff-56774e0e5ab1e847262c7a4fba42f12faec641b3a6aca7680bb0311fa283838d)    <br/> [JS](https://github.com/DWS-2026/project-grupo-2/commit/523cb33ef13880d972a7aa1c04f531ae2ad21791#diff-3dee7dcc2464eca4848acc79505d6b7ff0ef892537228ff35c65dc2cd021615c)               |
|5|                                       [Realización de vídeo](https://www.youtube.com/watch?v=M-PBvYJEMNM)                                        |                                                                                                                                                                 [Vídeo](https://www.youtube.com/watch?v=M-PBvYJEMNM)                                                                                                                                                                 |

---



## 🛠 **Práctica 2: Web con HTML generado en servidor**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/5cEnen_3Wmc)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Navegación y Capturas de Pantalla**

#### **Diagrama de Navegación**
La navegación es esencialmente la misma respecto a la anterior entrega.

#### **Capturas de Pantalla Actualizadas**


A continuación hay una serie de capturas con su correspondiente breve explicación de las vistas que han pasado por un cambio sustancial más allá de estar conectadas a base de datos.

#### **1. Header.**
![Header](images/header.png)

> Añadida con mustache una plantilla de encabezado para cada página con las opciones de navegación dentro de la web.

#### **2. Listado de posts.**
![Post_Listing](images/Post_Listing_2.png)

> Página de listado de posts que muestra las entradas de la base de datos y el primer álbum que hay enlazado a cada post.

#### **3. Vista del post.**
![Post_view](images/Post_view_2.png)

> Nueva vista para los posts. De nuevo está reflejado el álbum enlazado y están los campos del texto del post en sí y la sección de comentarios del post.

#### **4. Vista de los posts de un usuario.**
![User_Post_Listing](images/User_post_listing_2.png)

> Similar a la vista del listado de posts anterior con las opciones añadidas de editar y eliminar post.

#### **5. Acceso denegado.**
![Access_Denied](images/Access_Denied.png)

> Pantalla de error resultado de intentar acceder a unos datos para los que no se tiene permiso.

#### **6. Listado de users.**
![User_Listing](images/user_listing_2.png)

> Pantalla actualizada de listado de users a partir de la base de datos con la opción de ver perfil y eliminar usuario sólo accesibles para el administrador.

#### **7. Pantalla de edición de post.**
![Access Denied](images/edit_post_2.png)

> Formulario de edición de post con las opciones de cambiar todos los campos por separado de post. Ahora hay un campo nuevo donde se pueden añadir álbumes donde estarán todos los álbumes de la base de datos .

### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.8 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/DWS-2026/project-grupo-2.git
   cd [project-grupo-2]
   ```

2. **Esta aplicación está pensada para funcionar en Visual Studio Code. Si bien puede funcionar en otros IDEs, VSCode es el IDE para el que está comprobado que los siguientes pasos funcionan.**

3. **Configuración del IDE.**

Se necesitarán los paquetes de extensiones 'Spring Boot Extension Pack' y 'Extension Pack for Java'.

4. **Configuración de la base de datos.**

Si bien la gestión se puede hacer desde MySQL Workbench, lo mínimo sería que se dispusiese de MySQL Server, y, se ejecutasen los siguientes comandos en la consola:
  - CREATE DATABASE musicforum;
  - USE musicforum;
  - GRANT ALL PRIVILEGES ON musicforum.* TO 'musicuser'@'localhost';



#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin`, contraseña: `admin123`
- **Usuario Registrado**: usuario: `user`, contraseña: `user123`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

![Diagrama Entidad-Relación](images/diagrama_bd.png)
### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

![Diagrama de Clases](images/classes-diagram.png)

> [Descripción opcional del diagrama y relaciones principales]

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Lorena López Gallego Casilda]**

[Todo lo relacionado con la entidad User]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Registro de usuarios](https://github.com/DWS-2026/project-grupo-2/commit/469dec1c1f541c9d2fad0ed119d302806292bc4c)  | [LoginController.java](https://github.com/DWS-2026/project-grupo-2/blob/main/MusicForum/src/main/java/com/example/MusicForum/Controller/LoginController.java)   |
|2| [User antes de la bbdd](https://github.com/DWS-2026/project-grupo-2/commit/ddfc37fc7e148d480251b2b35b05ca4f72194b80)  | [UserController.java](https://github.com/DWS-2026/project-grupo-2/blob/main/MusicForum/src/main/java/com/example/MusicForum/Controller/UserController.java)   |
|3| [Editar perfil](https://github.com/DWS-2026/project-grupo-2/commit/dbcf9f097bafb18849ae1518354f2bf64726e6e9)  | [UserController.java](https://github.com/DWS-2026/project-grupo-2/blob/main/MusicForum/src/main/java/com/example/MusicForum/Controller/UserController.java)   |
|4| [Listado usuarios](https://github.com/DWS-2026/project-grupo-2/commit/5712114fd4d6cf6f11943970b89e49ae4b7fb869)  | [UserController.java](https://github.com/DWS-2026/project-grupo-2/blob/main/MusicForum/src/main/java/com/example/MusicForum/Controller/UserController.java)   |
|5| [Role admin](https://github.com/DWS-2026/project-grupo-2/commit/819c5d900f1ac70973ec6ca3f75d41fd09dff475)  | [Carpeta Controller]()   |

---

#### **Alumno 3 - [Óscar Rodríguez Pérez]**

[Durante esta práctica me he encargado de todo lo relacionado con la entidad Post y Comment y de añadir SpringSecurity]

| Nº    |                                                                             Commits                                                                              | Files      |
|:------------: |:----------------------------------------------------------------------------------------------------------------------------------------------------------------:| :------------:|
|1|                      [Añadido SpringSecurity](https://github.com/DWS-2026/project-grupo-2/commit/3df896489ac7cc8c5af48c7d3bc3f8e73fe74d28)                       | [Archivo1](URL_archivo_1)   |
|2|                                                 [Implementado la eliminación de usuarios y albumes](https://github.com/DWS-2026/project-grupo-2/commit/56d0332daf1904f39350fbfc740deee472c046772)                                                 | [Archivo2](https://github.com/DWS-2026/project-grupo-2/commit/56d0332daf1904f39350fbfc740deee472c04677)   |
|3| [Implementación de imagenes en bases de datos para la entidad post](https://github.com/DWS-2026/project-grupo-2/commit/aacc629b8cf24cec15cfb25b85c96991f963ef13) | [Archivo3](https://github.com/DWS-2026/project-grupo-2/commit/aacc629b8cf24cec15cfb25b85c96991f963ef13)   |
|4|                       [Cambiar de H2 a MySQL](https://github.com/DWS-2026/project-grupo-2/commit/876fe913c39188193dc601a35d911bffdc7e303c)                       | [Archivo4](https://github.com/DWS-2026/project-grupo-2/commit/876fe913c39188193dc601a35d911bffdc7e303c)   |
|5|             [Conectados las tablas en la base de datos](https://github.com/DWS-2026/project-grupo-2/commit/e265a09e1efb59ed524285803400940d29187921)             | [Archivo5](https://github.com/DWS-2026/project-grupo-2/commit/e265a09e1efb59ed524285803400940d29187921)   |


---

## 🛠 **Práctica 3: Incorporación de una API REST a la aplicación web, análisis de vulnerabilidades y contramedidas**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Documentación de la API REST**

#### **Especificación OpenAPI**
📄 **[Especificación OpenAPI (YAML)](/api-docs/api-docs.yaml)**

#### **Documentación HTML**
📖 **[Documentación API REST (HTML)](https://raw.githack.com/[usuario]/[repositorio]/main/api-docs/api-docs.html)**

> La documentación de la API REST se encuentra en la carpeta `/api-docs` del repositorio. Se ha generado automáticamente con SpringDoc a partir de las anotaciones en el código Java.

### **Diagrama de Clases y Templates Actualizado**

Diagrama actualizado incluyendo los @RestController y su relación con los @Service compartidos:

![Diagrama de Clases Actualizado](images/complete-classes-diagram.png)

#### **Credenciales de Usuarios de Ejemplo**

| Rol | Usuario | Contraseña |
|:---|:---|:---|
| Administrador | admin | admin123 |
| Usuario Registrado | user1 | user123 |
| Usuario Registrado | user2 | user123 |

### **Participación de Miembros en la Práctica 3**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

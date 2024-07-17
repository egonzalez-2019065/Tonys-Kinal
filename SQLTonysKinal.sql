/*
	Programador: Edwin Alexander González García 
    Carné: 2019065 
    Código Técnico: IN5AM 
    Fecha Creación: 28/03/2023
	Fecha Modificación: 23/05/2023 - 24/05/2023 - 27/05/2023 - 28/05/2023 - 06/06/2023
*/
Drop database if exists DBTonysKinal2019065;
Create database DBTonysKinal2019065;

Use DBTonysKinal2019065;

Create table Empresas(
	codigoEmpresa int auto_increment not null, 
    nombreEmpresa varchar(150) not null, 
    direccion varchar(150) not null, 
    telefono varchar(8),
    PRIMARY KEY PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int auto_increment not null, 
	descripcion varchar(50) not null,
    PRIMARY KEY PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int auto_increment not null, 
	descripcion varchar(100) not null, 
    PRIMARY KEY PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int auto_increment not null, 
    nombreProducto varchar(150),
    cantidad int not null,
    PRIMARY KEY PK_codigoProducto (codigoProducto)
);
Create table Empleados(
	codigoEmpleado int not null auto_increment, 
    numeroEmpleado int not null, 
    apellidosEmpleado varchar(150) not null, 
    nombresEmpleado varchar(150) not null, 
    direccionEmpleado varchar(150) not null, 
    telefonoContacto varchar(8) not null, 
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null, 
    PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
	CONSTRAINT FK_Empleados_TipoEmpleado FOREIGN KEY (codigoTipoEmpleado) 
		REFERENCES TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int auto_increment not null, 
    fechaServicio date not null, 
    tipoServicio varchar(100) not null,
    horaServicio Time not null,
    lugarServicio varchar(150),
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    PRIMARY KEY PK_codigoServicio (codigoServicio),
    CONSTRAINT FK_Servicios_Empresas FOREIGN KEY (codigoEmpresa) 
		REFERENCES Empresas(codigoEmpresa)
);

Create table Presupuestos(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null, 
    cantidadPresupuesto decimal(10,2) not null, 
    codigoEmpresa int not null, 
    PRIMARY KEY PK_codigoEmpresa (codigoPresupuesto),
    CONSTRAINT FK_Presupuestos_Empresas FOREIGN KEY (codigoEmpresa) 
		REFERENCES Empresas(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidadPlato int not null, 
    nombrePlato varchar(50) not null, 
    descripcionPlato varchar(150) not null, 
    precioPlato decimal(10,2) not null, 
    codigoTipoPlato int not null, 
    PRIMARY KEY PK_codigoPlato (codigoPlato),
    CONSTRAINT FK_Platos_TipoPlato FOREIGN KEY (codigoTipoPlato)
		REFERENCES TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_CodigoProducto int not null, 
    codigoPlato int not null, 
    codigoProducto int not null, 
    PRIMARY KEY PK_Productos_codigoProducto (Productos_CodigoProducto),
    CONSTRAINT FK_Productos_has_Platos_Productos FOREIGN KEY (codigoProducto) 
		REFERENCES Productos(codigoProducto), 
	CONSTRAINT FK_Productos_has_Platos_Platos FOREIGN KEY (codigoPlato) 
		REFERENCES Platos(codigoPlato)
);

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null, 
    codigoPlato int not null, 
    codigoServicio int not null, 
    PRIMARY KEY PK_Servicios_codigoServicio (Servicios_codigoServicio),
    CONSTRAINT FK_Servicios_has_Platos_Servicios FOREIGN KEY (codigoServicio) 
		REFERENCES Servicios(codigoServicio),
	CONSTRAINT FK_Servicios_has_Platos_Platos FOREIGN KEY (codigoPlato)
		REFERENCES Platos(codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoEmpleados int not null,
    codigoServicio int not null, 
    codigoEmpleado int not null, 
    fechaEvento date not null, 
    horaEvento time not null, 
    lugarEvento varchar(150) not null,
    PRIMARY KEY PK_Servicios_has_Empleados (Servicios_codigoEmpleados),
    CONSTRAINT FK_Servicios_has_Empleados_Servicios FOREIGN KEY (codigoServicio)
		REFERENCES Servicios (codigoServicio),
	CONSTRAINT FK_Servicios_has_Empleados_Empleados FOREIGN KEY (codigoEmpleado)
		REFERENCES Empleados(codigoEmpleado)
);


create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null, 
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
    
);

create table Login(
	usuarioMaster varchar(50) not null, 
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);
/* --------------------------------- PROCEDIMIENTOS ALMACENADOS ---------------------------------- */

-- ------------------------------------ USUARIO -----------------------------------------
-- ----------------------------------  Agregar Usuario ---------------------------------
Delimiter //
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
			in usuarioLogin varchar(50), in contrasena varchar(50))
	Begin
		Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			Values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    End //
Delimiter ;
call sp_AgregarUsuario('Edwin Alexander','González García','egonzalez','e123');
call sp_AgregarUsuario('Pedro','Armas','parmas','parmas');
-- ----------------------------------  Listar Usuarios ---------------------------------
Delimiter //
	Create procedure sp_ListarUsuarios()
		Begin 
			Select
				U.codigoUsuario, 
                U.nombreUsuario, 
                U.apellidoUsuario, 
                U.usuarioLogin, 
                U.contrasena
			From Usuario U;
        End //
Delimiter ; 

-- ----------------------------------  Agregar Empresa ---------------------------------
Delimiter // 
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(100), 
		in direccion varchar(150), 
        in telefono varchar(8))
    Begin 
		Insert into Empresas(nombreEmpresa, direccion, telefono)
			Values(nombreEmpresa, direccion, telefono);
    End//
Delimiter ;

call sp_AgregarEmpresa('Tigo','18 calle 24-20 Zona 10, Guatemala','24398432');
call sp_AgregarEmpresa('Cementos Progreso','Zona 6, Guatemala','21223344');
call sp_AgregarEmpresa('Hotel Barceló','7a Avenida 15-45, Guatemala','20988764');
call sp_AgregarEmpresa('Hotel Clarion Suites','14 calle 3-08, Guatemala','24213333');
call sp_AgregarEmpresa('Claro','7a avenida 12-39 Zona 1, Guatemala','22056295');

-- ----------------------------------  Listar Empresa ---------------------------------
Delimiter // 
	Create procedure sp_ListarEmpresas()
		Begin
			Select 
				E.codigoEmpresa,
                E.nombreEmpresa, 
                E.direccion, 
                E.telefono
				FROM Empresas E
                 order by codigoEmpresa asc;
        End//
Delimiter ; 
-- ----------------------------------  Buscar Empresa ---------------------------------
Delimiter //
	Create procedure sp_BuscarEmpresa(in codEmpresa int)
		Begin
			Select 
				E.codigoEmpresa,
                E.nombreEmpresa, 
                E.direccion, 
                E.telefono
				FROM Empresas E
					WHERE E.codigoEmpresa = codEmpresa;
        End//
Delimiter ; 
-- ----------------------------------  Editar Empresa ---------------------------------
Delimiter //
	Create procedure sp_EditarEmpresa(in codEmpresa int, 
		in nomEmpresa varchar(100), 
		in dir varchar(150), 
		in tel varchar(8))
        Begin
			Update Empresas E
				Set 
					E.nombreEmpresa = nomEmpresa,
                    E.direccion = dir, 
                    E.telefono = tel
                    Where 
						E.codigoEmpresa = codEmpresa;
        End// 
Delimiter ; 
-- ----------------------------------  Eliminar Empresa ---------------------------------
Delimiter //
	Create procedure sp_EliminarEmpresa(in codEmpresa int) 
		Begin 
			Delete From Empresas 
				Where codigoEmpresa = codEmpresa; 
        End // 
Delimiter ; 
-- ------------------------------------- Presupuestos ---------------------------------
-- ------------------------------------- Agregar Presupuesto ---------------------------------
 Delimiter //
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, 
		in cantidadPresupuesto decimal(10,2), 
		in codigoEmpresa int)
        Begin
			Insert into Presupuestos(fechaSolicitud, cantidadPresupuesto,codigoEmpresa) 
				Values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End//
 Delimiter ;
 call sp_AgregarPresupuesto('2023-03-28',10522.11,1);
 call sp_AgregarPresupuesto('2023-04-29',100543.89,2);
 call sp_AgregarPresupuesto('2023-05-12',15772.92,3);
 call sp_AgregarPresupuesto('2022-10-29',9845.27,4);
 call sp_AgregarPresupuesto('2022-12-28',1230.20,5);
-- ------------------------------------- Listar Presupuesto ---------------------------------
Delimiter //
	Create procedure sp_ListarPresupuestos()
		Begin
			Select 
				P.codigoPresupuesto, 
                P.fechaSolicitud, 
                P.cantidadPresupuesto, 
                E.codigoEmpresa 
				From Presupuestos P 
					Inner join Empresas E 
						on P.codigoEmpresa = E.codigoEmpresa
                        order by codigoPresupuesto asc;
        End//
Delimiter ;
-- -------------------------------------- Buscar Presupuesto --------------------------
Delimiter //
	Create procedure sp_BuscarPresupuesto(in codPresupuesto int)
		Begin
			Select 
				P.codigoPresupuesto, 
                P.fechaSolicitud, 
                P.cantidadPresupuesto, 
                E.codigoEmpresa 
				From Presupuestos P 
					Inner join Empresas E 
						on P.codigoEmpresa = E.codigoEmpresa
				Where P.codigoPresupuesto = codPresupuesto;
        End//
Delimiter ;
-- -------------------------------------- Editar Presupuesto --------------------------
Delimiter //
	Create procedure sp_EditarPresupuesto(in codPresupuesto int, 
		in fechaSoli date, 
        in cantidadPresu decimal(10,2))
        Begin
			Update Presupuestos P
				Set 
					P.fechaSolicitud = fechaSoli,
                    P.cantidadPresupuesto = cantidadPresu
                    Where P.codigoPresupuesto = codPresupuesto;
        End//
Delimiter ;
-- -------------------------------------- Eliminar Presupuesto --------------------------
Delimiter //
	Create procedure sp_EliminarPresupuesto(in codPresupuesto int)
		Begin
			Delete From Presupuestos 
				Where codigoPresupuesto = codPresupuesto;
        End//
Delimiter ; 
-- -------------------------------------- Tipo Empleado --------------------------
-- -------------------------------------- Agregar Tipo Empleado --------------------------
Delimiter //
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin
			Insert into TipoEmpleado(descripcion)
				Values (descripcion);
        End//
Delimiter ; 
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Limpieza');
call sp_AgregarTipoEmpleado('Guardia');
call sp_AgregarTipoEmpleado('Piloto');
-- -------------------------------------- Listar Tipo Empleado --------------------------
Delimiter //
	Create procedure sp_ListarTipoEmpleados()
        Begin
			Select 
				T.codigoTipoEmpleado,
                T.descripcion
					From TipoEmpleado T
                    order by codigoTipoEmpleado ASC;
        End//
Delimiter ;
-- -------------------------------------- Buscar Tipo Empleado --------------------------
Delimiter //
	Create procedure sp_BuscarTipoEmpleado(in codTipoEm int)
        Begin
			Select 
				T.codigoTipoEmpleado,
                T.descripcion
					From TipoEmpleado T
						Where T.codigoTipoEmpleado = codTipoEm;
        End//
Delimiter ;
-- -------------------------------------- Editar Tipo Empleado --------------------------
Delimiter //
	Create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, 
		in descrip varchar(50))
		Begin
			Update TipoEmpleado T
				Set 
					T.descripcion = descrip
						Where T.codigoTipoEmpleado = codTipoEmpleado;
        End//
Delimiter ; 
-- -------------------------------------- Eliminar Tipo Empleado --------------------------
Delimiter //
	Create procedure sp_EliminarTipoEmpleado(in codTipo int)
		Begin
			Delete From TipoEmpleado
				Where codigoTipoEmpleado = codTipo;
        End//
Delimiter ; 
-- -------------------------------------- Tipo Plato --------------------------
-- ---------------------------------- Agregar Tipo Plato --------------------------
Delimiter //
	Create procedure sp_AgregarTipoPlato(in descripcion varchar(50))
		Begin
			Insert into TipoPlato(descripcion)
				Values (descripcion);
        End//
Delimiter ; 
call sp_AgregarTipoPlato('Comida China');
call sp_AgregarTipoPlato('Comida Rápida');
call sp_AgregarTipoPlato('Comida Típica');
call sp_AgregarTipoPlato('Platillo Especial');
call sp_AgregarTipoPlato('Buffet');
-- ---------------------------------- Listar Tipo Plato --------------------------
Delimiter //
	Create procedure sp_ListarTipoPlatos()
        Begin
			Select 
				T.codigoTipoPlato,
                T.descripcion
					From TipoPlato T
                    Order by codigoTipoPLato ASC;
        End//
Delimiter ;
-- --------------------------------- Buscar Tipo Plato --------------------------
Delimiter //
	Create procedure sp_BuscarTipoPlato(in codigoTPlato int)
        Begin
			Select 
				T.codigoTipoPlato,
                T.descripcion
					From TipoPlato T
						Where T.codigoTipoPlato = codigoTPlato;
        End//
Delimiter ;
-- --------------------------------- Editar Tipo Plato --------------------------
Delimiter //
	Create procedure sp_EditarTipoPlato(in codTPlato int, 
		in descrip varchar(50))
		Begin
			Update TipoPlato T
				Set 
					T.descripcion = descrip
						Where T.codigoTipoPlato = codTPlato;
        End//
Delimiter ; 
-- -------------------------------------- Eliminar Tipo Plato --------------------------
Delimiter //
	Create procedure sp_EliminarTipoPlato(in codTipo int)
		Begin
			Delete From TipoPlato
				Where codigoTipoPlato = codTipo;
        End//
Delimiter ; 
-- ------------------------------------ Platos ---------------------------------------
-- -------------------------------- Agregar Plato --------------------------
Delimiter //
	Create procedure sp_AgregarPlato(in cantidadPlato int, 
		in nombrePlato varchar(50), 
		in descripcionPlato varchar(150), 
        in precioPlato decimal(10,2), 
        in codigoTipoPlato int)
        Begin
			Insert into Platos(cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				Values(cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End//
Delimiter ; 
call sp_AgregarPlato(40,'Arroz Chino','Un rico arroz frito, contiene pollo entre otros ingredientes',55.25,1);
call sp_AgregarPlato(15,'Hamburguesa','Una deliciosa hamburguesa con los ingredientes de la casa',60.50,2);
call sp_AgregarPlato(22,'Pepián','Un platillo para no olvidar nuestro sabor',50.00,3);
call sp_AgregarPlato(17,'Carne','Carne asada con un toque especial del chef',75.00,4);
call sp_AgregarPlato(9,'Pizza','Una deliciosa pizza con nuestros sabores especiales',65.00,2);
-- -------------------------------- Listar Platos --------------------------
Delimiter //
	Create procedure sp_ListarPlatos()
		Begin
			Select 
				P.codigoPlato,
                P.cantidadPlato,
                P.nombrePlato,
                P.descripcionPlato,
                P.precioPlato,
                T.codigoTipoPlato 
					From Platos P
                    Inner Join TipoPlato T
                    on P.codigoTipoPlato = T.codigoTipoPlato
                     order by codigoPlato asc;
        End//
Delimiter ; 
-- -------------------------------- Buscar Plato --------------------------
Delimiter //
	Create procedure sp_BuscarPlato(in codPlato int)
		Begin
			Select 
				P.codigoPlato,
                P.cantidadPlato,
                P.nombrePlato,
                P.descripcionPlato,
                P.precioPlato,
                T.codigoTipoPlato 
					From Platos P
                    Inner Join TipoPlato T
                    on P.codigoTipoPlato = T.codigoTipoPlato
				Where P.codigoPlato = codPlato;
        End//
Delimiter ;
-- ----------------------------- Editar Plato ----------------------------
Delimiter //
	Create procedure sp_EditarPlato(in codPlato int, 
		in cant int, 
		in nombre varchar(50), 
		in descripcion varchar(150), 
        in precio decimal(10,2))
        Begin
			Update Platos P
				Set 
					P.cantidadPlato = cant,
					P.nombreplato = nombre, 
					P.descripcionPlato = descripcion,
                    P.precioPlato = precio
                    Where 
						P.codigoPlato = codPlato;
        End//
Delimiter ; 
-- ----------------------------- Eliminar Plato ----------------------------
Delimiter //
	Create procedure sp_EliminarPlato(in codPlato int)
		Begin
			Delete from Platos 
				Where codigoPlato = codPlato;
        End//
Delimiter ; 
-- -------------------------------- Productos ----------------------------------
-- -------------------------------- Agregar Productos  --------------------------
Delimiter //
	Create procedure sp_AgregarProducto(in nombreProducto varchar(150), 
		in cantidad int)
        Begin
			Insert into Productos(nombreProducto, cantidad)
				Values(nombreProducto, cantidad);
        End//
Delimiter ; 
call sp_AgregarProducto('Arroz',10);
call sp_AgregarProducto('Lechuga',15);
call sp_AgregarProducto('Harina',13);
call sp_AgregarProducto('Papa',5);
call sp_AgregarProducto('Carne',12);
-- -------------------------------- Listar Productos --------------------------
Delimiter //
	Create procedure sp_ListarProductos()
		Begin
			Select 
				P.codigoProducto,
                P.nombreProducto,
                P.cantidad
				From Productos P
                 order by codigoProducto asc;
        End//
Delimiter ; 
-- -------------------------------- Buscar Producto --------------------------
Delimiter //
	Create procedure sp_BuscarProducto(in codProducto int)
		Begin
			Select 
				P.codigoProducto,
                P.nombreProducto,
                P.cantidad
				From Productos P
					Where P.codigoProducto = codProducto;
        End//
Delimiter ; 
-- ----------------------------- Editar Producto ----------------------------
Delimiter //
	Create procedure sp_EditarProducto(in codProducto int, 
		in nombre varchar(150), 
		in cantidad int)
        Begin
			Update Productos P
				Set 
					P.nombreProducto = nombre,
					P.cantidad = cantidad
                    Where 
						P.codigoProducto = codProducto;
        End//
Delimiter ; 
-- ----------------------------- Eliminar Producto  ----------------------------
Delimiter //
	Create procedure sp_EliminarProducto(in codProducto int)
		Begin
			Delete from Productos 
				Where codigoProducto = codProducto;
        End//
Delimiter ; 
-- --------------------------- Emplados -----------------------------------
-- --------------------------- Agregar Empleados -------------------------
Delimiter //
	Create procedure sp_AgregarEmpleado(in numeroEmpleado int, 
		in apellidosEmpleado varchar(150),
        in nombresEmpleado varchar(150),
        in direccionEmpleado varchar(150),
        in telefonoContacto varchar(8),
        in gradoCocinero varchar(50),
        in codigoTipoEmpleado int)
        Begin
			Insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado,  
				direccionEmpleado, 
				telefonoContacto, 
				gradoCocinero,
                codigoTipoEmpleado)
				Values(numeroEmpleado, apellidosEmpleado, nombresEmpleado,  
				direccionEmpleado, 
				telefonoContacto, 
				gradoCocinero,
                codigoTipoEmpleado);
        End//
Delimiter ; 
call sp_AgregarEmpleado(2023001,'Matías Tun','Marcos José','Barrio Joyabaj, Quiché','10010510','Chef',1);
call sp_AgregarEmpleado(2023002,'García Cotzajay','Humberto Real','El Barrio, Zona 6 Guatemala','33357078','Coffeboy',2);
call sp_AgregarEmpleado(2023003,'Tzoy Perez','Diego Alejandro','Los Olivos, Zona 18 Guatemala','22009087','Lava Platos',3);
call sp_AgregarEmpleado(2023004,'González Joj','Alberto','San Ángel, Zona 6 Guatemala','34758976','Guardia',4);
call sp_AgregarEmpleado(2023005,'Noriega Rosales','Tatiana Fernanada','Zona 2, Guatemala','40495612','Chef',1);
-- -------------------------------- Listar Empleados --------------------------
Delimiter //
	Create procedure sp_ListarEmpleados()
		Begin
			Select 
				E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                T.codigoTipoEmpleado
					From Empleados E
                    Inner Join TipoEmpleado T
                    on E.codigoTipoEmpleado = T.codigoTipoEmpleado
                     order by codigoEmpleado asc;
        End//
Delimiter ; 
-- -------------------------------- Buscar Empleado --------------------------
Delimiter //
	Create procedure sp_BuscarEmpleado(in codEmpleado int)
		Begin
			Select 
				E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                T.codigoTipoEmpleado
					From Empleados E
                    Inner Join TipoEmpleado T
                    on E.codigoTipoEmpleado = T.codigoTipoEmpleado
						Where E.codigoEmpleado = codEmpleado;
        End//
Delimiter ; 
-- ----------------------------- Editar Empleado ----------------------------
Delimiter //
	Create procedure sp_EditarEmpleado(in codEmpleado int,
		in numero int, 
		in apellidos varchar(150),
        in nombres varchar(150),
        in direccion varchar(150),
        in telefono varchar(8),
        in grado varchar(50))
        Begin
			Update Empleados E
				Set 
					E.numeroEmpleado = numero,
					E.apellidosEmpleado = apellidos,
					E.nombresEmpleado = nombres,
					E.direccionEmpleado = direccion,
					E.telefonoContacto = telefono,
					E.gradoCocinero = grado
                    Where 
						E.codigoEmpleado = codEmpleado;
        End//
Delimiter ; 
-- ----------------------------- Eliminar Empleado --------------------------
Delimiter //
	Create procedure sp_EliminarEmpleado(in codEmpleado int)
		Begin
			Delete from Empleados 
				Where codigoEmpleado = codEmpleado;
        End//
Delimiter ; 
-- ----------------------------- Servicios --------------------------
-- -------------------------------- Agregar Servicio --------------------------
Delimiter //
	Create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(100),
		in horaServicio time,
        in lugarServicio varchar(150),
        in telefonoContacto varchar(8),
		in codigoEmpresa int)
        Begin
			Insert into Servicios(fechaServicio, tipoServicio,horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				Values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End//
Delimiter ; 
call sp_AgregarServicio('2023-12-23','Convivio','20:00:00','Estadio Cementos Progreso, Zona 6, Guatemala','40407890',1);
call sp_AgregarServicio('2023-11-25','Cumpleaños','15:00:00','Salón las Maravillas, Zona 6','23478900',2);
call sp_AgregarServicio('2023-9-27','Boda','17:00:00','Polideportivo de Jocotales, Zona 6','40407890',3);
call sp_AgregarServicio('2023-08-28','Quince Años','13:00:00','Salón las Maravillas, Zona 6','40407890',4);
call sp_AgregarServicio('2023-08-10','Cumpleaños','9:00:00','Hotel Barceló, Zona 9','40407890',5);
-- -------------------------------- Listar Servcios --------------------------
Delimiter //
	Create procedure sp_ListarServicios()
		Begin
			Select 
				S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                E.codigoEmpresa
					From Servicios S
                    Inner Join Empresas E
                    on S.codigoEmpresa = E.codigoEmpresa
					order by codigoServicio asc;
        End//
Delimiter ;
-- -------------------------------- Buscar Servicio --------------------------
Delimiter //
	Create procedure sp_BuscarServicio(in codServicio int)
		Begin
			Select 
				S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                E.codigoEmpresa
					From Servicios S
                    Inner Join Empresas E
                    on S.codigoEmpresa = E.codigoEmpresa
						Where S.codigoServicio = codServicio;
        End//
Delimiter ; 
-- ----------------------------- Editar Servicio ----------------------------
Delimiter //
	Create procedure sp_EditarServicio(in codServicio int, 
		in fecha date, 
		in tipo varchar(100),
        in hora time,
        in lugar varchar(150),
        in telefono varchar(8))
        Begin
			Update Servicios S
				Set 
					S.fechaServicio = fecha,
					S.tipoServicio = tipo,
					S.horaServicio = hora,
					S.lugarServicio = lugar,
					S.telefonoContacto = telefono
                    Where 
						S.codigoServicio = codServicio;
        End//
Delimiter ; 
-- ----------------------------- Eliminar Servicio ----------------------------
Delimiter //
	Create procedure sp_EliminarServicio(in codServ int)
		Begin
			Delete from Servicios 
				Where codigoServicio = codServ;
        End//
Delimiter ; 
-- ------------------------------------ Productos has Platos  -----------------------------------------
-- ----------------------------------  Agregar Productos has Platos ---------------------------------
Delimiter // 
	Create procedure sp_AgregarProductos_has_Platos(in Productos_CodigoProducto int, in codigoPlato int, in codigoProducto int)
    Begin 
		Insert into Productos_has_Platos(Productos_CodigoProducto, codigoPlato, codigoProducto)
			Values(Productos_CodigoProducto, codigoPlato, codigoProducto);
    End//
Delimiter ;
call sp_AgregarProductos_has_Platos(1,1,1);
call sp_AgregarProductos_has_Platos(2,2,2);
call sp_AgregarProductos_has_Platos(3,3,4);
call sp_AgregarProductos_has_Platos(4,5,3);
call sp_AgregarProductos_has_Platos(5,4,5);
-- ----------------------------------  Listar Productos has Platos ---------------------------------
Delimiter // 
	Create procedure sp_ListarProductos_has_Platos()
		Begin
			Select 
				P.Productos_CodigoProducto,
                Pr.codigoProducto, 
                Pl.codigoPlato
				FROM Productos_has_Platos P
					Inner join Productos Pr
						on P.codigoProducto = Pr.codigoProducto
					Inner join Platos Pl
						on P.codigoPlato = Pl.codigoPlato
                         order by Productos_CodigoProducto asc;
        End//
Delimiter ; 

-- ----------------------------------  Buscar Productos has Platos ---------------------------------
Delimiter //
	Create procedure sp_BuscarProductos_has_Platos(in productos int)
		Begin
			Select 
				P.Productos_CodigoProducto,
                Pr.nombreProducto, 
                Pl.nombrePlato
				FROM Productos_has_Platos P
					Inner join Productos Pr
						on P.codigoProducto = Pr.codigoProducto
					Inner join Platos Pl
						on P.codigoPlato = Pl.codigoPlato
					Where P.Productos_CodigoProducto = productos;
        End//
Delimiter ; 
-- ----------------------------------  Eliminar Productos has Platos ---------------------------------
Delimiter //
	Create procedure sp_EliminarProductos_has_Platos(in codProduc int) 
		Begin 
			Delete From Productos_has_Platos 
				Where Productos_CodigoProducto = codProduc; 
        End // 
Delimiter ; 
-- ------------------------------------ Servicios has Platos  -----------------------------------------
-- ----------------------------------  Agregar Servicios has Platos ---------------------------------
Delimiter // 
	Create procedure sp_AgregarServicios_has_Platos(in Servicios_CodigoServicio int, in codigoPlato int, in codigoServicio int)
    Begin 
		Insert into Servicios_has_Platos(Servicios_CodigoServicio, codigoPlato, codigoServicio)
			Values(Servicios_CodigoServicio, codigoPlato, codigoServicio);
    End//
Delimiter ;
call sp_AgregarServicios_has_Platos(1,4,1);
call sp_AgregarServicios_has_Platos(2,3,1);
call sp_AgregarServicios_has_Platos(3,1,2);
call sp_AgregarServicios_has_Platos(4,5,2);
call sp_AgregarServicios_has_Platos(5,4,4);
-- ----------------------------------  Listar Servicios has Platos ---------------------------------
Delimiter // 
	Create procedure sp_ListarServicios_has_Platos()
		Begin
			Select 
				S.Servicios_CodigoServicio,
                Sr.codigoServicio, 
                Pl.codigoPlato
				FROM Servicios_has_Platos S
					Inner join Servicios Sr
						on S.codigoServicio = Sr.codigoServicio
					Inner join Platos Pl
						on S.codigoPlato = Pl.codigoPlato
                         order by Servicios_CodigoServicio asc;
        End//
Delimiter ; 
-- ----------------------------------  Buscar Servicios has Platos ---------------------------------
Delimiter //
	Create procedure sp_BuscarServicios_has_Platos(in servicios int)
		Begin
			Select 
				S.Servicios_CodigoServicio,
                Sr.tipoServicio, 
                Pl.nombrePlato
				FROM Servicios_has_Platos S
					Inner join Servicios Sr
						on S.codigoServicio = Sr.codigoServicio
					Inner join Platos Pl
						on S.codigoPlato = Pl.codigoPlato
					Where S.Servicios_CodigoServicio = servicios;
        End//
Delimiter ; 
-- ----------------------------------  Eliminar Servicios has Platos ---------------------------------
Delimiter //
	Create procedure sp_EliminarServicios_has_Platos(in servicios int) 
		Begin 
			Delete From Servicios_has_Platos 
				Where Servicios_CodigoServicio = servicios; 
        End // 
Delimiter ; 
-- ------------------------------------ Servicios has Empleados  -----------------------------------------
-- ----------------------------------  Agregar Servicios has Empleados ---------------------------------
Delimiter // 
	Create procedure sp_AgregarServicios_has_Empleados(in Servicios_codigoEmpleados int, 
		in codigoServicio int, 
        in codigoEmpleado int,
        in fechaEvento date, 
        in horaEvento time, 
        in lugarEvento varchar(150))
    Begin 
		Insert into Servicios_has_Empleados(Servicios_CodigoEmpleados, codigoServicio, codigoEmpleado,fechaEvento,horaEvento,lugarEvento)
			Values(Servicios_CodigoEmpleados, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
    End//
Delimiter ;
call sp_AgregarServicios_has_Empleados(1,1,1,'2023-12-23','20:00:00','Estadio Cementos Progreso, Zona 6, Guatemala');
call sp_AgregarServicios_has_Empleados(2,1,2,'2023-12-23','20:00:00','Estadio Cementos Progreso, Zona 6, Guatemala');
call sp_AgregarServicios_has_Empleados(3,2,4,'2023-11-25','15:00:00','Salón las Maravillas, Zona 6');
call sp_AgregarServicios_has_Empleados(4,2,5,'2023-11-25','15:00:00','Salón las Maravillas, Zona 6');
call sp_AgregarServicios_has_Empleados(5,1,3,'2023-12-23','20:00:00','Estadio Cementos Progreso, Zona 6, Guatemala');
-- ----------------------------------  Listar Servicios has Empleados ---------------------------------
Delimiter // 
	Create procedure sp_ListarServicios_has_Empleados()
		Begin
			Select 
				S.Servicios_CodigoEmpleados,
                Sr.codigoServicio, 
                E.codigoEmpleado,
                S.fechaEvento,
                S.horaEvento, 
                S.lugarEvento
				FROM Servicios_has_Empleados S
					Inner join Servicios Sr
						on S.codigoServicio = Sr.codigoServicio
					Inner join Empleados E
						on S.codigoEmpleado = E.codigoEmpleado
                         order by Servicios_CodigoEmpleados asc;
        End//
Delimiter ; 
-- ----------------------------------  Buscar Servicios has Empleados ---------------------------------
Delimiter //
	Create procedure sp_BuscarServicios_has_Empleados(in servicios int)
		Begin
			Select 
				S.Servicios_CodigoEmpleados,
                Sr.codigoServicio, 
                E.codigoEmpleado,
                S.fechaEvento,
                S.horaEvento, 
                S.lugarEvento
				FROM Servicios_has_Empleados S
					Inner join Servicios Sr
						on S.codigoServicio = Sr.codigoServicio
					Inner join Empleados E
						on S.codigoEmpleado = E.codigoEmpleado
					Where S.Servicios_CodigoEmpleados = servicios;
        End//
Delimiter ; 
-- ----------------------------------- Editar Servicios has Empleados -------------------------------
Delimiter //
	Create procedure sp_EditarServicios_has_Empleados(in servicios int, 
        in fecha date, 
        in hora time, 
        in lugar varchar(150))
        Begin
        Update Servicios_has_Empleados S
			Set 
				S.fechaEvento = fecha,
                S.horaEvento = hora,
                S.lugarEvento = lugar
                Where S.Servicios_CodigoEmpleados = servicios;
			End //
Delimiter ;
-- ----------------------------------  Eliminar Servicios has Empleados ---------------------------------
Delimiter //
	Create procedure sp_EliminarServicios_has_Empleados(in servicios int) 
		Begin 
			Delete From Servicios_has_Empleados 
				Where Servicios_CodigoEmpleados = servicios; 
        End // 
Delimiter ;  	

-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';

/*
select PL.nombrePlato, PL.cantidadPlato, PR.nombreProducto, PR.cantidad
	From Empresas E 
		Inner join Servicios S on S.codigoEmpresa = E.codigoEmpresa
		Inner join Servicios_has_Platos SE on SE.codigoServicio = S.codigoServicio
        Inner join Platos PL on PL.codigoPLato = SE.codigoPlato 
		Inner join Productos_has_Platos PP on PP.codigoPlato = PL.codigoPlato
		Inner join Productos PR on PR.codigoProducto = PP.codigoProducto
        where e.codigoEmpresa  = 1
        group by PP.Productos_CodigoProducto;
        
select E.nombreEmpresa, E.telefono, E.direccion, EM.nombresEmpleado, EM.telefonoContacto, TE.descripcion, S.tipoServicio, S.fechaServicio, S.lugarServicio
	From Empresas E 
		Inner join Servicios S on S.codigoEmpresa = E.codigoEmpresa
		Inner join Servicios_has_Empleados SE on SE.codigoServicio = S.codigoServicio
		Inner join Empleados EM on EM.codigoEmpleado = SE.codigoEmpleado
        Inner join TipoEmpleado TE on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado 
        where e.codigoEmpresa  = 1
        group by SE.Servicios_codigoEmpleados;
*/
package com.edutecno.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.edutecno.model.Users;

//para utilizar MyBatis como framework de consultas a la base de datos y de persistencia
//se necesita una interfaz (repositorio) Mapper que logre mapear (escanear) el objeto y se realicen las consultas
@Mapper
public interface UsuarioMapper {
	
	@Select("SELECT * FROM users WHERE email = #{email}")
	public Users findByEmail(@Param("email") String email);

}

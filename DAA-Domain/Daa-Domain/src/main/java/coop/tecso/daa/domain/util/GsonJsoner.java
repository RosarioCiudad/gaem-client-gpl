/*
 * Copyright (c) 2016 Municipalidad de Rosario, Coop. de Trabajo Tecso Ltda.
 *
 * This file is part of GAEM.
 *
 * GAEM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * GAEM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GAEM.  If not, see <http://www.gnu.org/licenses/>.
 */

package coop.tecso.daa.domain.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import coop.tecso.daa.domain.base.Jsoner;

/**
 * 
 * @author tecso.coop
 *
 */
public class GsonJsoner implements Jsoner {

	private GsonBuilder builder = builder(); 

	private GsonBuilder builder() {
		GsonBuilder builder = new GsonBuilder();
		// 
		builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
				return new JsonPrimitive(date.getTime());
			}
		});
		//
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
					throws JsonParseException {					
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			}});

		return builder;
	}

	@Override
	public String toJson(Object obj) {
		return builder.create().toJson(obj);
	}

	@Override
	public <T> T fromJson(String json, Class<T> klass) {
		return builder.create().fromJson(json, klass);
	}

	@Override
	public <T> T fromJson(String json, Type typeOf) {
		return builder.create().fromJson(json, typeOf);
	}

	public <T> T fromJson(JsonElement json, Class<T> klass) {
		return builder.create().fromJson(json, klass);
	}
}
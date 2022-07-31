package com.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student implements Serializable {

	private static final long serialVersionUID = 4528883226398538198L;

	private String id;
	private String firstName;
	private String lastName;
	private String age;
	private String gender;
	private LocalTime createTime;
}

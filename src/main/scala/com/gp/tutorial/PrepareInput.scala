package com.gp.tutorial

import com.gp.tutorial.student.{Address, Property, Student, Value}
import javax.xml.bind.DatatypeConverter

import scala.collection.mutable

object PrepareInput {
	val address = Address("3rd Street", "CA", 98029)
	val properties = Property(Map("key" -> Value()
		.withStringValue("foo")
		.withDoubleValue(2)
		.withLongValue(4l)))
	var students =
		new mutable.ListBuffer[Student]()

	(1 to 100)
		.foreach(i =>
			students :+= Student()
				.update(
					_
						.name := s"Joe_$i",
					_
						.age := 32,
					_
						.address := address,
					_
						.properties := properties,
					_
						.id := i)
		)

	def main(args: Array[String]) {
		students
			.foreach {
				p =>
					println(DatatypeConverter
						.printBase64Binary(p
							.toByteArray))
			}
	}

}

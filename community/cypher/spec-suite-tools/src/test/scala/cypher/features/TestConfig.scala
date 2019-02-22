/*
 * Copyright (c) 2002-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cypher.features

class TestConfig(
  val blacklist: Option[String],
  val executionPrefix: String
)

case object DefaultTestConfig extends TestConfig(Some("default.txt"),"")

case object CostSlottedTestConfig extends TestConfig(Some("cost-slotted.txt"),"CYPHER planner=cost runtime=slotted")

case object CostSlottedWithCompiledExpressionsTestConfig extends TestConfig(Some("cost-slotted.txt"),"CYPHER planner=cost runtime=slotted expressionEngine=COMPILED")

case object CostMorselSingleThreadedTestConfig extends TestConfig(Some("cost-morsel-single-threaded.txt"),"CYPHER planner=cost runtime=morsel debug=singlethreaded")

case object CostMorselTestConfig extends TestConfig(Some("cost-morsel.txt"),"CYPHER planner=cost runtime=morsel")

case object CostCompiledTestConfig extends TestConfig(Some("cost-compiled.txt"),
  "CYPHER planner=cost runtime=compiled debug=generate_java_source")

case object CostInterpretedTestConfig extends TestConfig(Some("cost-interpreted.txt"),"CYPHER planner=cost runtime=interpreted")

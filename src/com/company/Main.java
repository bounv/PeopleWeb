package com.company;
// import file reader
// create ArrayList<com.company.Person>
// file object = new file "person.csv"
// Scanner = file object

// while(Scanner_Ob_.HasNext() {
// String ___ Scanner Next Line ()
// String[] a _ _.split
// com.company.Person person = new com.company.Person(a[0], a[1])
// __.add(person)
// }

// Spark.get
// new object
// Iterate through First 20
// go Previous or Next
// (-1, +1) <ArrayList>

// Get com.company.Person
// get.person()

// Do positioning Id [0], First Name [1], etc..


import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Person> persons = new ArrayList<>();

        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);

        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");
            if(!"id".equals(columns[0])) {
                Person person = new Person(Integer.valueOf(columns[0]), columns[1], columns[2], columns[3], columns[4], columns[5]);
                persons.add(person);
            }
        }

        Spark.get(
                "/",
                ((request, response) -> {

                    String offsetString = request.queryParams("offset");
                    int offnum = 0;
                    if(offsetString != null && !offsetString.isEmpty()) {
                        offnum = Integer.parseInt(offsetString);
                    }

                    ArrayList<Person> personsSubList = new ArrayList<>();

                    for(int i = 0; i<20; i++) {
                         personsSubList.add(persons.get(offnum+i));
                    }

                    HashMap m = new HashMap();
                    m.put("people", personsSubList);

                    if(offnum-20 >= 0) {
                        m.put("prevOffnum", offnum-20);
                    }

                    if(offnum+20 < persons.size()) {
                        m.put("nextOffnum", offnum + 20);

                    }

                    return new ModelAndView(m, "index.html");

                }),
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/person",
                ((request, response) -> {
                    String idString = request.queryParams("id");

                    HashMap p = new HashMap();

                    for( Person person: persons) {
                        if(person.id == Integer.parseInt(idString)) {
                            p.put("person", person);
                        }
                    }

                    return new ModelAndView(p, "person.html");
                }),
                new MustacheTemplateEngine()
        );

    }
}

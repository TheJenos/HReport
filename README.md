# HReport[Java]
Java based HTML report file creating framework 

## Getting Started

it's simple to implement. just add the jar file to your project. That's all.

## Running
### How it's work
Make a html structure file.
```
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>PARA{title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body>
    <h1>PARA{title}</h1>
    <table border="1">
        <tr>
            <th>Name</th>
            <th>Text</th>
            <th>Date</th>
            <th>JS Date</th>
        </tr>
        <loop>
            <tr>
                <th>[[Name]]</th>
                <th>[[Text]]</th>
                <th>TIME{yyyy-MM-dd->[[Now]]}</th>
                <th>JS{return new Date();}</th>
            </tr>
        </loop>
    </table>
</body>

</html>
```
Make Hreport Object on java side.
```
 Hreport hr = new Hreport("test.html");
```
Make some data to display.
```
public class EnCaps {

    private String Name;
    private String Text;
    private Date now = new Date();

    public EnCaps(String Name, String Text) {
        this.Name = Name;
        this.Text = Text;
    }
    
    public Date getNow() {
        return now;
    }

    public String getName() {
        return Name;
    }

    public String getText() {
        return Text;
    }

}

HashMap<String, String> map = new HashMap<String, String>();
map.put("title", "JS{return new Date();}");
map.put("name", "Text");
map.put("id", "15");
ArrayList al = new ArrayList();
al.add(new EnCaps("Nadun", "test123"));
al.add(new EnCaps("Thanura", "test123"));
```
Compile the structure.
```
 hr.compile(map, al);
```
Then open it or print it.
```
hr.openFile();
//hr.printFile();
```
Also you can take output html string without making the file.
```
String outtxt = hr.toString();
```

## HTML Structure Custom Syntax
### SQL Tag
you have to add sql tag to work with connection based compilation.
```
<sql>query</sql>
```
Example
```
<sql>
    select * from product 
</sql>
```
### Loop Tag
you have to use Loop tag to loop throught the listed data. there is mechanism to fetch data from loops.

when you have to add some seperate loops like this.
```
 hr.compile(map, al, al2);
```
then you have to add loop tags like this.
```
 <loop[index]>
 </loop[index]>
```
```
 <loop>
 </loop>
 
 <loop1>
 </loop1>
```

#### inside of loop tag there are two ways to fetch.

* Bean List
* ResultSet Data


### Bean List
```
[[variable_name]]
[[Name]]
[[Text]]
```
it works like this.
```
obj.getvariable_name();
obj.getName();
obj.getText();
```

### ResultSet Data
```
[[Column_name]]
[[Name]]
[[Text]]
```
it works like this.
```
rs.getString("Column_name");
rs.getString("Name");
rs.getString("Text");
```

### PARA scope (PARA{})
this scope is very usefull for work with dynamic inputs.
```
<sql>
    select * from product where id='PARA{id}'
</sql>

<h1> PARA{title} </h1>

<loop>
  [[PARA{col}]]
</loop>
```

### JavaScript scope (JS{})
this scope helps you to calculate values or other stuffs
```
JS{return new Date();}

JS{return PARA{id}+12;}

<loop>
  JS{return [[id]]+12;}
</loop>
```

### Time scope (TIME{})
this scope helps you to format time output
```
TIME{yyyy-MM-dd->PARA{Now}}

<loop>
  TIME{yyyy-MM-dd->[[Now]]}
</loop>
```

## Built With

* HTML - report view
* Java8 - base language

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

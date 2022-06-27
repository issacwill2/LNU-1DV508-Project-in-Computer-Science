import mysql.connector as mysql
import sys

#establish the connection
try:
    mydb = mysql.connect(
        host = "localhost",
        # read db creds from cli args
        user = sys.argv[1],
        password = sys.argv[2])

    print("successfully connected to the database")
        
except:
    print("\nconnection failed \ncheck host ip address and credentials")
    sys.exit()

myCursor = mydb.cursor()


# create the cook book database 
myCursor.execute("CREATE DATABASE IF NOT EXISTS cookbook")


#select the Database
myCursor.execute("USE cookbook")


# create the user table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS user(
        id VARCHAR(36) PRIMARY KEY,
        display_name VARCHAR(50) NOT NULL,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(50) NOT NULL,
        is_admin BOOLEAN NOT NULL
    );
""")


# create the recipe table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS recipe(
        id VARCHAR(36)  PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        short_desc VARCHAR(255) NOT NULL,
        long_desc VARCHAR(10000) NOT NULL
    );
""")


# create the ingredient table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS ingredient(
        id VARCHAR(36) PRIMARY KEY,
        name VARCHAR(50) NOT NULL UNIQUE
    );
""")


# create the recipe_ingredient table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS recipe_ingredient(
        recipe_id VARCHAR(36),
        ingredient_id VARCHAR(36),
        unit VARCHAR(20),
        amount DECIMAL(8,2) NOT NULL,

        FOREIGN KEY fk_recipe_ingredient_recipe(recipe_id) REFERENCES recipe(id)
            ON UPDATE CASCADE 
            ON DELETE CASCADE,
        FOREIGN KEY fk_recipe_ingredient_ingredient(ingredient_id) REFERENCES ingredient(id)
            ON UPDATE CASCADE 
            ON DELETE CASCADE,

        CONSTRAINT pk PRIMARY KEY (recipe_id,ingredient_id)
    );
""")


# create tag table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS tag(
        id VARCHAR(36) PRIMARY KEY,
        name VARCHAR(50) NOT NULL UNIQUE
    );
""")

# create recipe_tag table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS recipe_tag(
        recipe_id VARCHAR(36),
        tag_id VARCHAR(36),
        FOREIGN KEY fk_recipe_tag_tag(tag_id)
            REFERENCES tag(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_recipe_tag_recipe(recipe_id)
            REFERENCES recipe(id)
                ON UPDATE CASCADE 
                ON DELETE CASCADE,
        CONSTRAINT pk PRIMARY KEY (recipe_id,tag_id)
    );
""")

# create comment table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS comment(
        id VARCHAR(36) PRIMARY KEY,
        body VARCHAR(255) NOT NULL,
        recipe_id VARCHAR(36) NOT NULL,
        user_id VARCHAR(36) NOT NULL,
        FOREIGN KEY fk_comment_recipe(recipe_id)
            REFERENCES recipe(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_comment_user(user_id)
            REFERENCES user(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE
    );
""")

# create user_starred table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS user_starred(
        user_id VARCHAR(36),
        recipe_id VARCHAR(36),
        FOREIGN KEY fk_user_starred_user(user_id)
            REFERENCES user(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_user_starred_recipe(recipe_id)
            REFERENCES recipe(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        CONSTRAINT pk PRIMARY KEY (recipe_id,user_id)
    );
""")


# create week_list
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS week_list(
        date DATETIME,
        recipe_id VARCHAR(36),
        user_id VARCHAR(36) NOT NULL,
        FOREIGN KEY fk_week_list_recipe(recipe_id)
            REFERENCES recipe(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_week_list_user(user_id)
            REFERENCES user(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        CONSTRAINT pk PRIMARY KEY (date, recipe_id, user_id)
    );
""")


## crete message table
myCursor.execute("""
    CREATE TABLE IF NOT EXISTS message(
        id VARCHAR(36) PRIMARY KEY,
        from_user_id VARCHAR(36) NOT NULL,
        to_user_id VARCHAR(36) NOT NULL,
        recipe_id VARCHAR(36),
        body VARCHAR(255),
        created_at DATETIME DEFAULT NOW(),
        FOREIGN KEY fk_message_user(from_user_id)
            REFERENCES user(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_message_user2(to_user_id)
            REFERENCES user(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
        FOREIGN KEY fk_message_recipe(recipe_id)
            REFERENCES recipe(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE
    );
""")

admin_args = ("9b3595d0-dab8-4bfc-b321-cb273a956e7c", "admin", "admin", "admin", True)
myCursor.execute("INSERT INTO user VALUES(%s, %s, %s, %s, %s);",tuple(admin_args))
mydb.commit()

user_args = "8dc4cc04-64b2-44cf-932b-99b554f1a9ee", "user", "user", "user", False
myCursor.execute("INSERT INTO user VALUES(%s, %s, %s, %s, %s);",tuple(user_args))
mydb.commit()

print ("\n done! \n")
                
                      

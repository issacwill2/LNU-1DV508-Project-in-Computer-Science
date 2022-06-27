import sys
import mysql.connector
from mysql.connector import errorcode
import datetime
from textwrap import dedent


# Check if db already exists, if not, create db
def connect():
    #establish the connection
    try:
        cnt = mysql.connector.connect(
            host = "localhost",
            # read db creds from cli args
            user = sys.argv[1],
            password = sys.argv[2]
        )
            
    except:
        print("\n connection failed \n check host ip address and credentials")
        sys.exit()

    db_name = "cookbook"
    cursor = cnt.cursor()

    try:
        cursor.execute(f"USE {db_name}")
        print(f"Successfully connected to database {db_name}")
    except mysql.connector.Error as error:
        print(f"Database {db_name} does not exist")

    return(cursor, cnt)

# User data dictionary
# Requires a user_name, password, id
# Stored in the format {id:(username, password)}
# 10 users are used in db seeder


users = {
    "0c45845a-3562-49df-a759-dbe0f1a6644a":("Cameron Turner", "Cameron Turner", "J(7#BjMfuW", False),
    "473c3a6b-54ec-44d1-a5c9-63e1f9ef698f":("Theresa Terrell","Theresa Terrell", "+uj2CYy4Z1", False),
    "ffe34034-e322-4d52-8cc7-1c66d85c0bb8":("Alyssa Estes", "Alyssa Estes", "$BCAEFbAh7", False),
    "40445a38-5f75-4224-b985-f04d36d53e43":("James Anderson", "James Anderson", "183wfBkyL(", False),
    "11f25fac-78bd-467e-a7b1-804e7d125fc3":("Matthew Wood", "Matthew Wood", "@^g6_Yim(d", False),
    "3acddab8-c34e-401d-8ae4-fd370feee9f4":("Austin Brown", "Austin Brown", "4h0U8qNU#x", False),
    "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94":("Laura Byrd", "Laura Byrd", "#j0n7Qv&J(", False),
    "3baf3148-0735-47c6-9533-6856222044c7":("Patricia Ortega", "Patricia Ortega", "M3oDDSRr!#", False),
    "bd52430d-1cd0-4304-b2b1-95dc7c4b255d":("Laura Martinez", "Laura Martinez", "pi9oSgAas^", False),
    "db710b4d-5db0-4c63-a699-21b92e50e0e8":("Jeremiah Oliver", "Jeremiah Oliver", "Q$+j3VTSVl", False),
}



# Recipe Dictionary
# Requires a id, name, short_desc, long_desc
# Stored in the format {id:(name, short_desc, long_desc)}

recipes = {
# https://www.bbcgoodfood.com/recipes/chilli-con-carne-recipe
    "648b8840-086e-4dd7-932f-fbbfc0935649":(
        "Chilli con carne",

        """
        This great chilli recipe has to be one of the best dishes 
        to serve to friends for a casual get-together. An easy sharing
        favourite that uses up storecupboard ingredients.
        """,

        """
        Prepare your vegetables. Chop 1 large onion into small dice, about 5mm square.
        The easiest way to do this is to cut the onion in half from root to tip, peel it and
        slice each half into thick matchsticks lengthways, not quite cutting all the way to 
        the root end so they are still held together. Slice across the matchsticks into neat dice.\n

        Cut 1 red pepper in half lengthways, remove stalk and wash the seeds away, then chop. Peel 
        and finely chop 2 garlic cloves. Start cooking. Put your pan on the hob over a medium heat. 
        Add 1 tbsp oil and leave it for 1-2 minutes until hot (a little longer for an electric hob).\n

        Add the onion and cook, stirring fairly frequently, for about 5 minutes, or until the onion is
        soft, squidgy and slightly translucent.\n
        
        Tip in the garlic, red pepper, 1 heaped tsp hot chilli powder or 1 level tbsp mild chilli powder,
        1 tsp paprika and 1 tsp ground cumin.\n

        Give it a good stir, then leave it to cook for another 5 minutes, stirring occasionally.\n

        Brown 500g lean minced beef. Turn the heat up a bit, add the meat to the pan and break it up 
        with your spoon or spatula. The mix should sizzle a bit when you add the mince.\n

        Keep stirring and prodding for at least 5 minutes, until all the mince is in uniform, mince-sized
        lumps and there are no more pink bits. Make sure you keep the heat hot enough for the meat to fry 
        and become brown, rather than just stew.\n

        Make the sauce. Crumble 1 beef stock cube into 300ml hot water. Pour this into the pan with the 
        mince mixture.\n    

        Add a 400g can of chopped tomatoes. Tip in ½ tsp dried marjoram, 1 tsp sugar and add a good 
        shake of salt and pepper. Squirt in about 2 tbsp tomato purée and stir the sauce well.\n

        Simmer it gently. Bring the whole thing to the boil, give it a good stir and put a lid on 
        the pan. Turn down the heat until it is gently bubbling and leave it for 20 minutes.\n

        Check on the pan occasionally to stir it and make sure the sauce doesn't catch on the bottom
        of the pan or isn't drying out. If it is, add a couple of tablespoons of water and make sure 
        that the heat really is low enough. After simmering gently, the saucy mince mixture should 
        look thick, moist and juicy.\n

        Drain and rinse a 410g can of red kidney beans in a sieve and stir them into the chilli pot. 
        Bring to the boil again, and gently bubble without the lid for another 10 minutes, adding a 
        little more water if it looks too dry.\n

        Taste a bit of the chilli and season. It will probably take a lot more seasoning than you think.\n

        Now replace the lid, turn off the heat and leave your chilli to stand for 10 minutes before serving.
        This is really important as it allows the flavours to mingle.\n

        Serve with soured cream and plain boiled long grain rice.\n
        """
    ),

# https://www.deliciousmagazine.co.uk/recipes/sweet-and-spicy-chicken-skewers/
    "22f483c9-00bb-488c-aa3d-2cc2f47f6747":(
        "Sweet and spicy chicken skewers",

        """
        Marinated in yogurt, honey and mustard then grilled to charred, smoky perfection, 
        serve these chicken skewers as a light starter or summer canapé.
        """,

        """
        In a large bowl, mix the marinade ingredients with a pinch of salt and pepper until
        well combined. Remove half the marinade to another small bowl, cover with cling film and chill.\n

        Add the chicken to the marinade in the large bowl and toss to coat. Cover with cling film and 
        chill for at least 1 hour, but preferably overnight.\n

        Heat the grill to high. When ready, thread 3 pieces of chicken onto each skewer, folding them over
        so they're skewered twice (discard the marinade). Put the kebabs on a foil-lined baking tray and grill
        for 10-15 minutes, turning once, until cooked. Serve with the reserved marinade from step 1 as a dip.\n
        """
    ),

# https://realfood.tesco.com/recipes/nathan-outlaws-one-pot-roast-chicken-and-chorizo.html
    "958465f8-5eda-4238-95f7-c3b248a7a71d":(
        "One-pot Roast Chicken",

        """
        Introduce your children to the flavours of the Mediterranean with this combination 
        of chicken and chorizo.
        """,

        """
        Preheat the oven to gas 7, 220°C, fan 200°C.\n
        
        Place the chicken, skin-side up, in a roasting tin with sweet potato chunks.\n
        
        Leaving the skin on lightly crush the garlic cloves and ask the children to place them amongst the 
        pieces of chicken.\n
        
        Drizzle the rapeseed oil over the pan and add the chicken stock, then season. Get the kids to squeeze
        the lemon juice in too before adding the juiced halves of lemon to the tray.\n
        
        Bake in the oven for 45-50 minutes. About 30 minutes in add the chorizo, courgettes and chilli.\n
        
        Cut into the thickest part of the thigh to check that the chicken is cooked through with no pink 
        showing, then remove from the oven and allow to rest for a few minutes.\n
        
        Mix the spinach and parsley leaves through the hot chicken mix and serve immediately.\n
        """
    ),

# https://www.deliciousmagazine.co.uk/recipes/griddled-vegetable-and-halloumi-burger-with-chilli-yoghurt/
    "3224021f-8ec2-4f59-ad04-f899e04cb210":(
        "Griddled vegetable & halloumi burger",

        """
        A fuss-free, cheap midweek recipe, packed with fresh, summer flavours.
        """,

        """
        Heat a griddle pan to high. Cut the courgettes and aubergine into thin strips. Cut the onion into 8 wedges and 
        the halloumi into 8 slices.\n
        
        Brush the aubergines with oil on both sides, season with salt and pepper, then griddle for 2-3 minutes on both 
        sides until tender and striped (don't crowd the griddle pan, cook in batches if necessary). Remove and keep warm.
        Repeat with the courgettes. Toss the onion with the vinegar and griddle as above, then grill the halloumi for 10-20 
        seconds on each side. Halve the rolls and toast lightly, cut-side down, on the griddle pan for 20 seconds.\n
        
        Meanwhile mix the yogurt and chilli sauce in a bowl.\n
        
        To make each burger put the courgettes, aubergine, halloumi and onions on the base, then top with the chilli yogurt 
        and rocket. Add the roll tops and serve.\n
        """
    )
}



# tags available for recipes
# tags are in the form {tag_id:{tag_name}
# recipe_tags are in the form 
tags = {
        "cb309ae1-f097-4b6a-aabb-af52a05bebb2":"Spicy",
        "0bae1403-f8dd-4fde-b715-d2e3063246d6":"Medium cook time",
        "447e540f-a607-4010-bbf6-aad1be55bd0f":"Fast cook time",
        "c9cc3153-429a-4a44-833a-5cbe6e3e80d7":"Low Fat",
        "21e7fa6e-6fd5-434d-9266-e8b09be28b3b":"Low Saturated Fat",
        "706641fa-a491-4796-8c1a-7f0d9e268f3b":"Low Sugar",
        "3dbeaa17-9832-4f5f-8f4c-6bbe8641b213":"High Fat",
         "79fc7332-8008-466e-b79d-a8a89c2f842a":"High Saturated Fat",
        "6fb1a141-c203-47d1-a531-c71c0c46ec2e":"High protein",
        "e2f3cf4c-6d1c-489c-987d-57b6b3406fc9":"Grilled",
        "4e796376-7ec4-45fc-9a63-bf8500882ba1":"Dairy Free",
        "02e31bbd-530a-4664-9088-479c6e2fc3aa":"Vegetarian"
}


recipe_tags = {
    "648b8840-086e-4dd7-932f-fbbfc0935649":(
        "cb309ae1-f097-4b6a-aabb-af52a05bebb2",
        "c9cc3153-429a-4a44-833a-5cbe6e3e80d7",
        "21e7fa6e-6fd5-434d-9266-e8b09be28b3b",
        "706641fa-a491-4796-8c1a-7f0d9e268f3b",
        "0bae1403-f8dd-4fde-b715-d2e3063246d6"
    ),

    "22f483c9-00bb-488c-aa3d-2cc2f47f6747":(
        "447e540f-a607-4010-bbf6-aad1be55bd0f",
        "e2f3cf4c-6d1c-489c-987d-57b6b3406fc9",
        "c9cc3153-429a-4a44-833a-5cbe6e3e80d7",
        "21e7fa6e-6fd5-434d-9266-e8b09be28b3b",
        "706641fa-a491-4796-8c1a-7f0d9e268f3b"
    ),

    "958465f8-5eda-4238-95f7-c3b248a7a71d":(
        "4e796376-7ec4-45fc-9a63-bf8500882ba1",
        "0bae1403-f8dd-4fde-b715-d2e3063246d6",
        "706641fa-a491-4796-8c1a-7f0d9e268f3b",
        "3dbeaa17-9832-4f5f-8f4c-6bbe8641b213",
        "79fc7332-8008-466e-b79d-a8a89c2f842a",
        "6fb1a141-c203-47d1-a531-c71c0c46ec2e"
    ),

    "3224021f-8ec2-4f59-ad04-f899e04cb210":(
        "cb309ae1-f097-4b6a-aabb-af52a05bebb2",
        "447e540f-a607-4010-bbf6-aad1be55bd0f",
        "6fb1a141-c203-47d1-a531-c71c0c46ec2e",
        "02e31bbd-530a-4664-9088-479c6e2fc3aa"
    )
}



# Ingredients for each recipe
# Requires id, name, amount

ingredients = {
    "963eed9e-f95a-4b9c-88d5-4ec36c43fc3b":"onion",
    "828b11c6-7a33-4216-9f19-0d2898b30b4f":"red pepper",
    "e0b854de-5c2c-4fd5-9a57-0f197067f9c3":"garlic clove",
    "df8eab90-a2fa-47f7-96f7-0dd01f1042df":"oil",
    "c35213c6-4890-4d29-9b3a-445730eaa9b6":"chilli powder",
    "1946d5d6-3c17-4d75-9271-7e1d406d8570":"paprika",
    "7c564e8b-889f-4980-b04a-a66b7869e98e":"ground cumin",
    "48ba07e1-a346-46e0-ac3b-3559c3f9e979":"minced beef",
    "1b5c0f52-5d15-4eca-ac31-fc95b60cc0ae":"beef stock cube",
    "34192839-ea8f-482f-8777-f79010ffde74":"chopped tomotoes",
    "7df549f3-bb8f-465c-a399-d257af3776b3":"dried marjoram",
    "4668f59b-7cd8-4a5e-b6b6-456730416b12":"sugar",
    "fc5c46ea-20db-47c1-9acf-3c75b958fbf4":"tomato purée",
    "b372f0ee-0a94-457d-a2b9-c3b4e60721e7":"kidney beans",
    "2d9b4c0a-cb64-49a1-94c3-e16549deae5e":"long grain rice",
    "59c9b849-33cf-4102-b029-ab7b296a33d1":"sour cream",
    "1a4e78a7-7a9a-4d60-8de1-d0fa80f52158":"natural yogurt",
    "358934fa-8182-4003-afca-30c66dac83b9":"clear honey",
    "11560091-ecda-480d-8f2b-594a689df853":"wholegrain mustard",
    "c297674d-7f56-40c1-a8e5-b2d13b0e8089":"dijon mustard",
    "1845ba88-08e5-4cee-99a0-570e15c07499":"cayyenne pepper",
    "8eb51a32-1894-49d7-90cb-6f2f0ac00f83":"skewers",
    "f0ddd3c0-e6fe-4017-b5f6-1a1420e04d69":"chicken thighs",
    "a1021acb-c793-4006-a681-15fed90943b3":"sweet potato",
    "444f61d5-209e-4fa5-a9c5-52e9eb078a60":"chorizo sausage",
    "cffae490-e887-4063-9ec3-ac1ee1ce0c0e":"whole garlic",
    "2a2c1a9e-5e3d-46a8-bc54-9e5d919ad0ec":"rapseed oil",
    "bb02972f-ca4c-4672-bffd-4dfe6c6af68e":"chicken stock",
    "0426a926-4df7-4447-b920-06636a0405eb":"lemon",
    "4d5565f4-6a3f-454f-813e-5a57e70ccb70":"courgettes",
    "640a69e0-a2dc-4075-97c9-12bf85ae1085":"red chilli",
    "19521fd4-7f84-4008-940e-6297dba78dfd":"baby spinach",
    "38a5a349-a629-4de5-8af5-463237ccb822":"parsley",
    "54ccba5a-772e-4cf4-b3e8-c2331009a86e":"salt",
    "c80dc072-3b6a-4599-a665-b0acf2f3c706":"black pepper",
    "4d5565f4-6a3f-454f-813e-5a57e70ccb70":"courgettes",
    "bef26af3-e959-4941-807a-b030f61861d9":"eggplant",
    "864cba1e-b3a9-4c95-9ed9-2814ec805cce":"balsamic vinegar",
    "edea3cdc-2f0f-4818-8111-c871edfe0995":"halloumi",
    "1aa297a7-6113-4a51-84ef-ed9fb68963dc":"olive oil",
    "ad509e80-51c6-4929-be6a-923027a96b68":"bread rolls",
    "02c96f1c-69e7-40b7-a4b2-04dbd8af4f2a":"greek yogurt",
    "334e8744-8d99-4259-8cf8-5c6d8d1d7348":"chilli sauce"
}

recipe_one_ingredient_details = {
    "963eed9e-f95a-4b9c-88d5-4ec36c43fc3b":("onion", None, 1),
    "828b11c6-7a33-4216-9f19-0d2898b30b4f":("red pepper", None, 1),
    "e0b854de-5c2c-4fd5-9a57-0f197067f9c3":("garlic", "clove", 2),
    "df8eab90-a2fa-47f7-96f7-0dd01f1042df":("oil", "tablespoon", 1),
    "c35213c6-4890-4d29-9b3a-445730eaa9b6":("chilli powder", "tablespoon", 1),
    "1946d5d6-3c17-4d75-9271-7e1d406d8570":("paprika", "teaspoon", 1),
    "7c564e8b-889f-4980-b04a-a66b7869e98e":("ground cumin", "teaspoon", 1),
    "48ba07e1-a346-46e0-ac3b-3559c3f9e979":("minced beef", "gram", 500),
    "1b5c0f52-5d15-4eca-ac31-fc95b60cc0ae":("beef stock cube", None, 1),
    "34192839-ea8f-482f-8777-f79010ffde74":("chopped tomotoes", "gram", 400),
    "7df549f3-bb8f-465c-a399-d257af3776b3":("dried marjoram", "teaspoon", 1),
    "4668f59b-7cd8-4a5e-b6b6-456730416b12":("sugar", "teaspoon", 1),
    "fc5c46ea-20db-47c1-9acf-3c75b958fbf4":("tomato purée", "tablespoon", 2),
    "b372f0ee-0a94-457d-a2b9-c3b4e60721e7":("kidney beans", "gram", 410),
    "2d9b4c0a-cb64-49a1-94c3-e16549deae5e":("long grain rice", "serving", 1),
    "59c9b849-33cf-4102-b029-ab7b296a33d1":("sour cream", "dollop",1)
}


recipe_two_ingredient_details = {
    "1a4e78a7-7a9a-4d60-8de1-d0fa80f52158":("natural yogurt", "gram", 500),
    "358934fa-8182-4003-afca-30c66dac83b9":("clear honey", "tablespoon", 3),
    "11560091-ecda-480d-8f2b-594a689df853":("wholegrain mustard", "tablespoon", 2),
    "c297674d-7f56-40c1-a8e5-b2d13b0e8089":("dijon mustard", "tablespoon", 2),
    "0426a926-4df7-4447-b920-06636a0405eb":("lemon", None, 1),
    "1845ba88-08e5-4cee-99a0-570e15c07499":("cayyenne pepper", "teaspoons", 1),
    "8eb51a32-1894-49d7-90cb-6f2f0ac00f83":("skewers", None, 20)
}


recipe_three_ingredient_details = {
    "f0ddd3c0-e6fe-4017-b5f6-1a1420e04d69":("chicken thighs", None, 8),
    "a1021acb-c793-4006-a681-15fed90943b3":("sweet potato", "gram", 800),
    "444f61d5-209e-4fa5-a9c5-52e9eb078a60":("chorizo sausage", "gram", 200),
    "cffae490-e887-4063-9ec3-ac1ee1ce0c0e":("whole garlic", None,1),
    "2a2c1a9e-5e3d-46a8-bc54-9e5d919ad0ec":("rapseed oil", "tablespoon", 2),
    "bb02972f-ca4c-4672-bffd-4dfe6c6af68e":("chicken stock", "ml",200),
    "0426a926-4df7-4447-b920-06636a0405eb":("lemon", None,1),
    "4d5565f4-6a3f-454f-813e-5a57e70ccb70":("courgettes", None,2),
    "640a69e0-a2dc-4075-97c9-12bf85ae1085":("red chilli", None, 1),
    "19521fd4-7f84-4008-940e-6297dba78dfd":("baby spinach", "gram", 240),
    "38a5a349-a629-4de5-8af5-463237ccb822":("parsley", "tablespoon",2),
    "54ccba5a-772e-4cf4-b3e8-c2331009a86e":("salt", "pinch", 1),
    "c80dc072-3b6a-4599-a665-b0acf2f3c706":("black pepper", "pinch", 1)
}


recipe_four_ingredient_details = {
    "4d5565f4-6a3f-454f-813e-5a57e70ccb70":("courgettes", None, 2),
    "963eed9e-f95a-4b9c-88d5-4ec36c43fc3b":("onion", None, 1),
    "bef26af3-e959-4941-807a-b030f61861d9":("eggplant", None, 1),
    "864cba1e-b3a9-4c95-9ed9-2814ec805cce":("balsamic vinegar", "tablespoon", 1),
    "edea3cdc-2f0f-4818-8111-c871edfe0995":("halloumi", "gram", 100),
    "1aa297a7-6113-4a51-84ef-ed9fb68963dc":("olive oil", "tablespoon", 3),
    "ad509e80-51c6-4929-be6a-923027a96b68":("bread rolls", None, 4),
    "02c96f1c-69e7-40b7-a4b2-04dbd8af4f2a":("greek yogurt", "gram", 60),
    "334e8744-8d99-4259-8cf8-5c6d8d1d7348":("chilli sauce", "tbalespoon", 1.5)
}


recipe_ingredients = {
    "648b8840-086e-4dd7-932f-fbbfc0935649":recipe_one_ingredient_details, "22f483c9-00bb-488c-aa3d-2cc2f47f6747": recipe_two_ingredient_details,
    "958465f8-5eda-4238-95f7-c3b248a7a71d":recipe_three_ingredient_details, "3224021f-8ec2-4f59-ad04-f899e04cb210":recipe_four_ingredient_details
    }



# Get list of user ids and randomly assign them to one another for messaging.
# messages are in the form {id: (from, to, body, recipe_id/None)}

messages = {
    "fbf7b002-2970-4c47-aa4e-9d7e859dc73f":("bd52430d-1cd0-4304-b2b1-95dc7c4b255d", "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94", 
        "I know you'll love this!", "648b8840-086e-4dd7-932f-fbbfc0935649"),

    "13e267d1-66c1-4ab9-a2c4-64e632ec7f23":("ffe34034-e322-4d52-8cc7-1c66d85c0bb8", "3baf3148-0735-47c6-9533-6856222044c7",
        "brunch?", "3224021f-8ec2-4f59-ad04-f899e04cb210"),

    "14e6b4ef-e3ee-4ce4-8865-74e41c6581d7":("3baf3148-0735-47c6-9533-6856222044c7", "11f25fac-78bd-467e-a7b1-804e7d125fc3",
        "Try it out maybe.", "22f483c9-00bb-488c-aa3d-2cc2f47f6747"),

    "9db8f0a9-67ed-4feb-a665-2df3ea95e542":("11f25fac-78bd-467e-a7b1-804e7d125fc3", "ffe34034-e322-4d52-8cc7-1c66d85c0bb8",
        "I think you'd like this", "22f483c9-00bb-488c-aa3d-2cc2f47f6747"),

    "769937cc-e7b2-40d7-b337-897ea1fe9c58":("bd52430d-1cd0-4304-b2b1-95dc7c4b255d", "40445a38-5f75-4224-b985-f04d36d53e43",
        "I know you'll love this!", "648b8840-086e-4dd7-932f-fbbfc0935649"),

    "1551a721-b182-4f63-b43c-ca325545842c":("0c45845a-3562-49df-a759-dbe0f1a6644a", "db710b4d-5db0-4c63-a699-21b92e50e0e8",
        "What do you want for dinner tomorrow?", "648b8840-086e-4dd7-932f-fbbfc0935649"),

    "4754d4c8-27c9-42d7-bc81-316a268ad22b":("0c45845a-3562-49df-a759-dbe0f1a6644a", "db710b4d-5db0-4c63-a699-21b92e50e0e8",
        "I'm thinking Barbeque", "3224021f-8ec2-4f59-ad04-f899e04cb210"),

    "ebad1a01-636d-412c-ae5b-fc0840bf59ca":("db710b4d-5db0-4c63-a699-21b92e50e0e8", "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94",
        "How about this for lunch tomorrow?", "22f483c9-00bb-488c-aa3d-2cc2f47f6747"),

    "d39f3067-7769-4d29-b7a3-f1b25d687b5c":("bd52430d-1cd0-4304-b2b1-95dc7c4b255d", "40445a38-5f75-4224-b985-f04d36d53e43",
        "Maybe you could make it on Sunday?", "958465f8-5eda-4238-95f7-c3b248a7a71d"),

    "7b40b0ae-ba55-404a-8ef8-23666f32b3c0":("3acddab8-c34e-401d-8ae4-fd370feee9f4", "bd52430d-1cd0-4304-b2b1-95dc7c4b255d",
        "This is exactly your kinda stuff", "958465f8-5eda-4238-95f7-c3b248a7a71d"),

    "5f883a46-4f26-418c-904c-19908fb0b2c1":("bd52430d-1cd0-4304-b2b1-95dc7c4b255d", "3acddab8-c34e-401d-8ae4-fd370feee9f4",
        "You know me so well!", "958465f8-5eda-4238-95f7-c3b248a7a71d"),

    "e728a081-caf0-4dd9-b1bd-db0ca501c831":("bd52430d-1cd0-4304-b2b1-95dc7c4b255d", "11f25fac-78bd-467e-a7b1-804e7d125fc3",
        "What are thinking for dinner tomorrow?", "648b8840-086e-4dd7-932f-fbbfc0935649")
}


# comments under recipes
# comments are in the form {comment_id:(user_id, recipe_id, body)}
comments = {
    "a796a3a7-c39f-47e5-a704-7f5c2c04a3e1":(
        "0c45845a-3562-49df-a759-dbe0f1a6644a",
        "22f483c9-00bb-488c-aa3d-2cc2f47f6747",
        "These are perfect for weekend BBQs!"
    ),
    "7dfb2fcf-21a5-4c12-aaed-b4572ac5e7dd":(
        "3baf3148-0735-47c6-9533-6856222044c7",
        "22f483c9-00bb-488c-aa3d-2cc2f47f6747",
        "I wish we could have these everyday"
    ),



    "85903a55-859a-4e65-b2cf-122da3c1f3e4":(
        "3acddab8-c34e-401d-8ae4-fd370feee9f4",
        "3224021f-8ec2-4f59-ad04-f899e04cb210",
        "God I love halloumi"
    ),
    "c12aef64-79ca-413c-b10c-50b40ca34362":(
        "db710b4d-5db0-4c63-a699-21b92e50e0e8",
        "3224021f-8ec2-4f59-ad04-f899e04cb210",
        "The yoghurt is SUCH a good addition."
    ),
    "9d8c7d85-51b0-4fc8-8209-f884d118bf1d":(
        "11f25fac-78bd-467e-a7b1-804e7d125fc3",
        "3224021f-8ec2-4f59-ad04-f899e04cb210",
        "This is my favourite thing during a busy week."
    ),


    "4364e0dd-c15c-4d9f-9478-a91eff2ada9b":(
        "40445a38-5f75-4224-b985-f04d36d53e43",
        "648b8840-086e-4dd7-932f-fbbfc0935649",
        "WAY to spicy for me, but sooooo good."
    ),
    "cf85878b-3f1f-4032-b701-cef6cab67dcc":(
        "ffe34034-e322-4d52-8cc7-1c66d85c0bb8",
        "648b8840-086e-4dd7-932f-fbbfc0935649",
        "Goes right through me but couldn't stop eating it"
    )
}


# starred recipes for users
# starred recipes are in the form {user_id:recipe_id}
starred = {
    "db710b4d-5db0-4c63-a699-21b92e50e0e8":"22f483c9-00bb-488c-aa3d-2cc2f47f6747",
    "bd52430d-1cd0-4304-b2b1-95dc7c4b255d":"22f483c9-00bb-488c-aa3d-2cc2f47f6747",
    "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94":"3224021f-8ec2-4f59-ad04-f899e04cb210",
    "ffe34034-e322-4d52-8cc7-1c66d85c0bb8":"648b8840-086e-4dd7-932f-fbbfc0935649",
    "3acddab8-c34e-401d-8ae4-fd370feee9f4":"22f483c9-00bb-488c-aa3d-2cc2f47f6747",
    "0c45845a-3562-49df-a759-dbe0f1a6644a":"22f483c9-00bb-488c-aa3d-2cc2f47f6747",
    "40445a38-5f75-4224-b985-f04d36d53e43":"3224021f-8ec2-4f59-ad04-f899e04cb210",
}


"""
Standby uuids {Made to copy faster}:
c52eede2-3248-4e7b-9493-8a42ea1284c7
8df9e4fb-f9ea-4817-b4b4-74213284dbb1
fa373787-41fb-410f-aa86-55fbcc6dc835
75a1f354-1670-4313-9e27-c6fa41823005
9dbad6b7-1b87-4f20-b1f9-3f3e49fbb357
874e5663-77d0-4c48-be74-747d947ce030
0ed0a89d-6c8b-41c7-927e-a5a063e45968
"""


# Week list for users
# week list is in the form {date:recipe_id}
week = {
    "2022-04-25":("22f483c9-00bb-488c-aa3d-2cc2f47f6747", "11f25fac-78bd-467e-a7b1-804e7d125fc3"),
    "2022-04-26":("22f483c9-00bb-488c-aa3d-2cc2f47f6747", "3acddab8-c34e-401d-8ae4-fd370feee9f4"),
    "2022-04-27":("3224021f-8ec2-4f59-ad04-f899e04cb210", "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94"),
    "2022-04-28":("648b8840-086e-4dd7-932f-fbbfc0935649", "11f25fac-78bd-467e-a7b1-804e7d125fc3"),
    "2022-04-29":("22f483c9-00bb-488c-aa3d-2cc2f47f6747", "3acddab8-c34e-401d-8ae4-fd370feee9f4"),
    "2022-04-30":("22f483c9-00bb-488c-aa3d-2cc2f47f6747", "4b38ca0e-6f0a-4699-92cd-5da2b33cbe94"),
    "2022-05-01":("3224021f-8ec2-4f59-ad04-f899e04cb210", "db710b4d-5db0-4c63-a699-21b92e50e0e8"),
}


##-------------------------------------------------------------------------------------------------##
##                                                                                                 ##
##                                      SEED FUNCTIONS                                             ##
##                                                                                                 ##
## Each data dictionary is iterated over and the each data row is input into the db                ##
##-------------------------------------------------------------------------------------------------##


def seed_user_table(cursor, cnt):
    for user_id in users:
        display_name, user_name, user_password, is_admin = users[user_id]
        user_name = user_name.lower().replace(" ", "")
        user_args = (user_id, display_name, user_name, user_password, is_admin)
        try:
            cursor.execute("INSERT INTO user VALUES(%s, %s, %s, %s, %s);",tuple(user_args))
        except mysql.connector.Error as err:
            print(err.msg)
        
        cnt.commit()


def seed_recipe_table(cursor, cnt):
    for recipe_id in recipes:
        recipe_name, recipe_short_desc, recipe_long_desc = recipes[recipe_id]
        recipe_args = (recipe_id, recipe_name, dedent(recipe_short_desc), dedent(recipe_long_desc))
        try:
            cursor.execute("INSERT INTO recipe VALUES(%s, %s, %s, %s);",tuple(recipe_args))
        except mysql.connector.Error as err:
            print(err.msg)
        
        cnt.commit()

def seed_ingredients_table(cursor, cnt):
    for ingredient_id in ingredients:
        ingredient_name = ingredients[ingredient_id]
        ingredient_args = (ingredient_id, ingredient_name)

        try:
            cursor.execute("INSERT INTO ingredient VALUES(%s, %s);",tuple(ingredient_args))
        except mysql.connector.Error as err:
            print(err.msg)

        cnt.commit()



def seed_recipe_ingredients_table(cursor, cnt):
    for recipe_id in recipe_ingredients:
        ingredient_dict = recipe_ingredients[recipe_id]
        for ingredient_id in ingredient_dict:
            _, unit, amount = ingredient_dict[ingredient_id]
            recipe_ingredient_args = (recipe_id, ingredient_id, unit, float(amount))

            try:
                cursor.execute("INSERT INTO recipe_ingredient VALUES(%s, %s, %s, %s);",tuple(recipe_ingredient_args))
            except mysql.connector.Error as err:
                print(err.msg)
            
            cnt.commit()


def seed_message_table(cursor, cnt):
    # {id: (from, to, body, recipe_id/None)}
    for message_id in messages:
        from_id, to_id, body, recipe_id = messages[message_id]
        message_args = (message_id, from_id, to_id, recipe_id, body, datetime.datetime.now())

        try:
            cursor.execute("INSERT INTO message VALUES(%s, %s, %s, %s, %s, %s);",tuple(message_args))
        except mysql.connector.Error as err:
            print(err.msg)

        cnt.commit()


def seed_tag_table(cursor, cnt):
    for tag_id in tags:
        tag_name = tags[tag_id]
        tag_args = (tag_id, tag_name)

        try:
            cursor.execute("INSERT INTO tag VALUES(%s, %s);",tuple(tag_args))
        except mysql.connector.Error as err:
            print(err.msg)

        cnt.commit()


def seed_recipe_tag_table(cursor, cnt):
    # tags are in the form {recipe-id:(tag_id_tuple)}
    for recipe_id in recipe_tags:
        tag_tup = recipe_tags[recipe_id]
        for tag_id in tag_tup:
            recipe_tag_args = (recipe_id, tag_id)

            try:
                cursor.execute("INSERT INTO recipe_tag VALUES(%s, %s);",tuple(recipe_tag_args))
            except mysql.connector.Error as err:
                print(err.msg)
            cnt.commit()


def seed_comment_table(cursor, cnt):
    # comments are in the form {comment_id:(user_id, recipe_id, body)}
    for comment_id in comments:
        user_id, recipe_id, body = comments[comment_id]
        comment_args = (comment_id, body, recipe_id, user_id)

        try:
            cursor.execute("INSERT INTO comment VALUES(%s, %s, %s, %s);",tuple(comment_args))
        except mysql.connector.Error as err:
            print(err.msg)

        cnt.commit()


def seed_starred_recipe_table(cursor, cnt):
    # starred recipes are in the form {user_id:recipe_id}
    for user_id in starred:
        recipe_id = starred[user_id]
        starred_args = (user_id, recipe_id)

        try:
            cursor.execute("INSERT INTO user_starred VALUES(%s, %s);",tuple(starred_args))
        except mysql.connector.Error as err:
            print(err.msg)
        cnt.commit()


def seed_week_list_table(cursor, cnt):
    # week list is in the form {date:recipe_id}
    for date in week:
        recipe_id, user_id = week[date]
        week_list_args = (date, recipe_id, user_id)

        try:
            cursor.execute("INSERT INTO week_list VALUES(%s, %s, %s);",tuple(week_list_args))
        except mysql.connector.Error as err:
            print(err.msg)
        cnt.commit()


##-------------------------------------------------------------------------------------------------##
##                                                                                                 ##
##-------------------------------------------------------------------------------------------------##


def Main():
    # Connect to db, exit if error
    cursor, cnt = connect()
    
    # Seed db
    seed_user_table(cursor, cnt)
    seed_ingredients_table(cursor, cnt)
    seed_recipe_table(cursor, cnt)
    seed_tag_table(cursor, cnt)

    seed_recipe_tag_table(cursor, cnt)
    seed_recipe_ingredients_table(cursor, cnt)
    seed_message_table(cursor, cnt)
    
    seed_comment_table(cursor, cnt)
    seed_starred_recipe_table(cursor, cnt)
    seed_week_list_table(cursor, cnt)
    


Main()

trace
domains
 position = symbol
 
predicates
 surname_position(symbol, symbol, symbol)
 has_sister(symbol)
 older_than(symbol)
 name(symbol)
 
 
clauses
    name("Borisov").
    name("Ivanov").
    name("Semenov").
    has_sister("Borisov").
    older_than("Semenov").
           
	surname_position(Locksmith, Turner, Welder):-
        name(Locksmith),
        name(Turner), Turner <> Locksmith,
        name(Welder), Welder <> Turner, Welder <> Locksmith,
        not(has_sister(Locksmith)),
        not(older_than(Locksmith)),
        not(older_than(Turner)).

goal
        surname_position(Locksmith, Turner, Welder),
            writef("% - locksmith, % - turner, % - welder\n", 
                Locksmith, Turner, Welder),
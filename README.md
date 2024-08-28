JourneyPlanner vznikl jako testování takové mé hypotézy o tom, jak by asi mohl fungovat vyhledávač spojení, bez kdovíjaké rešerše nebo zkoumání. 
Aplikace používá technologie představené na setkáních KPRO2, tedy Spring boot a H2 databázi. Databázi mám nastavenou s ukládáním do souboru, v tuhle chvíli by měla být testovací data.

Model entit víceméně odpovídá tomu, jak se data zadávají na frontendu - entita pro Linku (ve významu "jednosměrnou množinu všech spojů, co mají stejné zastávky a stejné jízdní doby mezi nimi") má podřízenou kolekci Zastávek.
Entita pro zastávky jen agreguje zastávku a dobu příjezdu a odjezdu na ní od zastávky počáteční. Frontend nicméně používá samostatné seznamy pro tyto 3 - snadněji se pak sestavuje graf pro vyhledávání.
Samotné spoje se definují ve formátu jízdního řádu, čísla oddělená mezerou pro každou hodinu reprezentují odjezd v minutách z výchozí zastávky.
Zastávky různých linek se stejným jménem jsou vyhledávačem považovány za totožné. Frontend pomocí datalistu napovídá existující názvy zastávek při vyhledávání i při editování.
DTO pro linku si držím jako sessionový atribut, aby bylo možné přidávat/odebírat zastávky a nepřijít o mezitím upravená data (dělám při obojím submit - ale do DB uložím jen na "save").
Nepodařilo se mi nastavit tlačítko "back" jako link - i jako link ta mrcha dělala submit, jakmile byla součástí formu - takže je to vyřešeno skriptem na onclick.

Při stisknutí vyhledávání se ze všech dat linek sestaví orientovaný neohodnocený graf, kde vrchol je "zastávkominuta", a hrana je spojnice zastávkominut, mezi kterými je možné se přesunout. 
Nejprve se instanciují všechny zastávkominuty - graf je teoreticky nekonečný, ale aplikace sestaví 2 po sobě jdoucí dny pro možnost vyhledávat i přes půlnoc - počet vrcholů v grafu tedy bude počet zastávek * 1440 * 2.
Následně se spojí hranami vrcholy, mezi kterými je možné se přesunout čekáním - například z vrcholu "hlavní nádraží 7:10" je možné se dostat do "hlavní nádraží 7:11" prostým čekáním.
Pak se propojí vrcholy, mezi kterými jede nějaký spoj. Propojeny by byly vrcholy sousedních zastávek s příslušnou jízdní dobou.
Chtěl jsem ale implementovat minimální čas na přestup, takže jsem musel pro každou zastávkominutu udělat vrcholy 2 - příjezdový a odjezdový, a propojit je adekvátně podle času na přestup - anebo vůbec, pokud chceme jen přímá spojení.
To ale znamená, že už nelze spoj reprezentovat jako prostou posloupnost hran. Musím vždy spojit odjezdový vrchol s příjezdovými, což už vyžaduje to udělat pro každou kombinaci příjezdu a odjezdu pro každý spoj zvlášť.
Vzniká tedy n^2 hran, kde n je počet zastávek spoje. Toto pro každý spoj každé linky. Je to dost hran, ale těch zastávek nikdy není kdovíjak kolik a hrana je jen pointer na vrchol v seznamu sousedů, takže asi cajk.

Vyhledávácí algoritmus si najde výchozí zastávkohranu a bude si držet v mapě (minuta -> list vrcholů) všechny objevené vrcholy. Hned do ní přidá sousedy výchozího vrcholu.
Pro každou následující minutu se pak pro každý vrchol objevený v té minutě přidají do mapy jeho sousedi, dokud nenarazíme na cílovou zastávku. 
Nutně takto bude nalezen vrchol s nejdřívějším časem, ve kterém je možné se do cíle dostat.
Zároveň s tím si držím v jiné mapě (vrchol -> vrchol) info o tom, ze kterého vrcholu jsme se tam dostali - ze kterého tam vede nejkratší cesta. 
Možností ale může být více, proto je mapa ve skutečnosti (vrchol -> list vrcholů).
Po nalezení cíle je nutné zpětně sestavit cestu, kudyma jsme se tam dostali. Tedy ne jednu, ale všechny možnosti. 
Zmíněná mapa nejkratších cest vlastně představuje další graf, který je potřeba prohledat do hloubky a zaevidovat všechny možné cesty.

Minimální čas přestupu nejde nastavit na 0, protože by pak algoritmus rozpoznával jako 2 různé cesty možnosti zastávkou buďto projet, anebo tam vystoupit, 0 minut čekat a nastoupit dál do stejného spoje. 
To by pak při zpětném hledání všech možných tras vytvářelo 2^n možností tras, kde n je počet mezilehlých zastávek nalezeného spojení. 
Takže jsem se na to vykašlal a prostě to zakázal. Je tam na tom validace i na straně serveru (podobně jako při zadání neexistující zastávky či stejného startu i cíle).

Mojí filozofií bylo nesnažit se vidět do hlavy uživatele a předvídat, co chce radši vidět, a tak mu ukážu všechny možnosti. Pokud je více možností, jak se do cíle dostat v nejkratší možný čas, ukážu všechny.
I když jedna většinou znamená čekat na výchozí zastávce déle, zatímco jiná znamená vyrazit dříve.
Výchozí nastavení vyhledávání hledá 5 výsledků spojení. O to se stará servisa, která provolává hledání v grafu, dokud nebude nalezených spojení alespoň 5. 
Může jich být ve výsledku zobrazeno více, pokud poslední hledání vrátí více možností.

Testovací data v sobě mají 2 brněnské šalinlinky s letním, prázdninovým provozem s výlukou na Bystrc. Mám papíry na tramvaje a vedlejšku u brněnského podniku, takže si občas něco zařídím a vyznám se tam v tom.
Linky byly zvoleny tak, aby tvarově na mapě připomínaly přeškrtlé V a šlo v nich dobře ukazovat, že program umí řešit přestupy. Vymodelováno je pár důležitějších zastávek, zdaleka ne všechny.
Jízdní doby jsou realistické, jízdní řád intervaly odpovídá jednotlivým denním dobám prázdninového provozu, ale odjezdy na minuty nejsou podle skutečnosti.

Inu to asi vše ohledně mého JourneyPlannera. Jsem rád, že algoritmus, co mě vlastně napadl jen tak, opravdu funguje.
Ale nemám třeba tušení, jak bych do toho implementoval něco jako garantované návaznosti - možnosti přestupu pouze mezi konkrétnímí spoji i tam, kde by nevyšla minimální doba přestupu...

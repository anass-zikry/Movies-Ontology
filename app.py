import tkinter as tk
from tkinter import ttk
import ttkbootstrap as ttk
from rdflib import Graph, URIRef
from rdflib.query import ResultRow

SOURCE = "http://www.semanticweb.org/anass/ontologies/movies"
NS = SOURCE + "#"
rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
ont = "http://www.semanticweb.org/anass/ontologies/movies#"
rdfs = "http://www.w3.org/2000/01/rdf-schema#"
PREFIX_rdf = f"PREFIX rdf:<{rdf}>";
PREFIX_ont = f"PREFIX ont:<{ont}>";
PREFIX_rdfs = f"PREFIX rdfs:<{rdfs}>";

g = Graph().parse("./movies.rdf")
actors = g.objects(NS + "actor", NS + "name")
actorType = URIRef(NS + "actor")
directorType = URIRef(NS + "director")
genreType = URIRef(NS + "genre")
typePrefix = URIRef(PREFIX_rdf + "type")
prefixes = """
    PREFIX ont:<http://www.semanticweb.org/anass/ontologies/movies#>
    PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>

    """
actorsQuery = prefixes + """
SELECT ?v
WHERE {
    ?actor rdf:type ont:actor .
    ?actor ont:name ?v .
}"""
directorsQuery = prefixes + """
SELECT ?v
WHERE {
    ?director rdf:type ont:director .
    ?director ont:name ?v .
}"""
genreQuery = prefixes + """
SELECT ?v
WHERE {
    ?genre rdf:type ont:genre .
    ?genre ont:genre ?v .
 }"""

allActors = g.query(actorsQuery)
allDirectors = g.query(directorsQuery)
allGenres = g.query(genreQuery)


included={'actors':[], 'directors':[], 'genres':[]}
excluded={'actors':[], 'directors':[], 'genres':[]}


def add_item(table, d):
    selected_item = treeview.focus()
    if selected_item:
        parent_item = treeview.parent(selected_item)
        if len(parent_item) > 0:
            item_text = treeview.item(selected_item)['text']
            d[parent_item].append(item_text)
            print("Selected item:", item_text)

            for child in table.get_children():
                if table.item(child)['values'][0] == item_text:
                    print("Item already exists in included items table.")
                    return
            table.insert(parent='', index=tk.END, values=(item_text,))
        else:
            print("can't add parent.")
    else:
        print("No item selected.")


def remove_selected_item(table, d:dict):
    selected_item = table.focus()
    item_text = table.item(selected_item)['values'][0]
    print(item_text)
    if selected_item:
        print(item_text)
        for key, lst in d.items():
            if item_text in lst:
                lst.remove(item_text)
                print(f"Removed '{item_text}' from {key}")
        table.delete(selected_item)
        print("Selected item removed.")
    else:
        print("No item selected for removal.")

def show_result():
    ######Generate Queries
    includeQuery = prefixes + """
    SELECT DISTINCT ?movieTitle
    WHERE {
        ?movie rdf:type ont:movie .

    """
    for i, actorName in enumerate(included['actors']):
        includeQuery = includeQuery + f"?actor{i} rdf:type ont:actor .\n?actor{i} ont:name \"{actorName}\" .\n?movie ont:hasActor ?actor{i} .\n"
    for i, directorName in enumerate(included['directors']):
        includeQuery = includeQuery + f"?director{i} rdf:type ont:director .\n?director{i} ont:name \"{directorName}\" .\n?movie ont:hasDirector ?director{i} .\n"
    for i, genre in enumerate(included['genres']):
        includeQuery = includeQuery + f"?genre{i} rdf:type ont:genre .\n?genre{i} ont:genre \"{genre}\" .\n?movie ont:hasGenre ?genre{i} .\n"
    includeQuery = includeQuery + "?movie ont:title ?movieTitle .\n}"
    # print(includeQuery)
    excludeQuery = prefixes + """
    SELECT DISTINCT ?movieTitle
    WHERE {
        ?movie rdf:type ont:movie .

    """
    for i, actorName in enumerate(excluded['actors']):
        excludeQuery = excludeQuery + f"?actor{i} rdf:type ont:actor .\n?actor{i} ont:name \"{actorName}\" .\n?movie ont:hasActor ?actor{i} .\n"
    for i, directorName in enumerate(excluded['directors']):
        excludeQuery = excludeQuery + f"?director{i} rdf:type ont:director .\n?director{i} ont:name \"{directorName}\" .\n?movie ont:hasDirector ?director{i} .\n"
    for i, genre in enumerate(excluded['genres']):
        excludeQuery = excludeQuery + f"?genre{i} rdf:type ont:genre .\n?genre{i} ont:genre \"{genre}\" .\n?movie ont:hasGenre ?genre{i} .\n"
    excludeQuery = excludeQuery + "?movie ont:title ?movieTitle .\n}"

    # if len(includedActors)==0 and len(includedDirectors)==0 and len(includedGenres)==0:
    #     print("nothing included")
    # else:
    ###### excute queries and retreive results from ontology
    print(includeQuery)
    includedMovies = g.query(includeQuery)
    doExclude = True
    if len(excluded['actors']) == 0 and len(excluded['directors']) == 0 and len(excluded['genres']) == 0:
        doExclude = False
    else:
        excludedMovies = g.query(excludeQuery)
        doExclude = True
    includedMovieTitles = []
    for m in includedMovies:
        includedMovieTitles.append(m["movieTitle"])
    excludedMovieTitles = []
    if doExclude:
        for m in excludedMovies:
            excludedMovieTitles.append(m["movieTitle"])

    for mT in excludedMovieTitles:
        if mT in includedMovieTitles:
            includedMovieTitles.remove(mT)
    print("incccccccccc:"+includeQuery)
    for mT in includedMovieTitles:
        print(mT)
    result_window = ttk.Toplevel(root)
    WIDTH, HEIGHT = 600, 400
    POS_X = round(root.winfo_screenwidth() / 2 - WIDTH / 2)
    POS_Y = round(root.winfo_screenheight() / 2 - HEIGHT / 2)

    result_window.geometry(f'{WIDTH}x{HEIGHT}+{POS_X}+{POS_Y}')
    result_window.minsize(550, 300)

    result_label = ttk.Label(result_window, text="Result", font='Calibri 16')
    result_label.pack()
    result_str = ""
    for mT in includedMovieTitles:
        result_str += mT
        result_str += "\n"
    result = ttk.Label(result_window, text=result_str, font='Calibri 13')
    result.pack(pady=10)


# mian window
root = ttk.Window()
WIDTH, HEIGHT = 800, 700
POS_X = round(root.winfo_screenwidth() / 2 - WIDTH / 2)
POS_Y = round(root.winfo_screenheight() / 2 - HEIGHT / 2)

root.title('Ontologies Project')
root.geometry(f'{WIDTH}x{HEIGHT}+{POS_X}+{POS_Y}')
root.minsize(550, 700)
# root.bind('<Escape>', lambda event: root.quit())

root.columnconfigure(0, weight=2)
root.columnconfigure(1, weight=8)

root.rowconfigure(0, weight=1)
root.rowconfigure(1, weight=3)
root.rowconfigure(2, weight=3)
root.rowconfigure(3, weight=3)
root.rowconfigure(4, weight=3)
root.rowconfigure(5, weight=3)
root.rowconfigure(6, weight=2)


upper_frame = ttk.Frame(root)
upper_frame.grid(row=0, column=0, columnspan=2, sticky='nsew')

upper_label = ttk.Label(upper_frame, text='Movies Ontology', font='Calibri 28 bold')
upper_label.pack(pady=20)


left_frame = ttk.Frame(root, borderwidth=20, relief=tk.GROOVE)
left_frame.grid(row=1, column=0, rowspan=4, sticky='nsew')
treeview = ttk.Treeview(left_frame, show='tree')
treeview.pack(expand=True, fill='both')

treeview.insert('', '0', 'actors', text='Actors')
treeview.insert('', '1', 'directors', text='Directors')
treeview.insert('', '2', 'genres', text='Genres')

for r in allActors:
    treeview.insert('actors', 'end', r[0], text =r[0])
for r in allDirectors:
    treeview.insert('directors', 'end', r[0], text =r[0])
for r in allGenres:
    treeview.insert('genres', 'end', r, text=r)


first_right_frame = ttk.Frame(root, borderwidth=20, relief=tk.GROOVE)
first_right_frame.grid(row=1, column=1, rowspan=2, sticky='nsew')

include_items_label = ttk.Label(first_right_frame, text='Include Movie: ', font='Calibri 14')
include_items_label.pack(expand=True, fill='x')

included_items_table = ttk.Treeview(first_right_frame, columns=('item'), show='tree')
included_items_table.column("#0", width=100, anchor="w")

include_items_remove_button = ttk.Button(include_items_label, text='Remove', command=lambda: remove_selected_item(included_items_table, included))
include_items_remove_button.pack(side='right', padx=5, pady=5)

include_items_add_button = ttk.Button(include_items_label, text='Add', command=lambda: add_item(included_items_table, included))
include_items_add_button.pack(side='right', padx=5, pady=5)

included_items_table.pack(expand=True, fill='both')



second_right_frame = ttk.Frame(root, borderwidth=20, relief=tk.GROOVE)
second_right_frame.grid(row=3, column=1, rowspan=2, sticky='nsew')

excluded_items_label = ttk.Label(second_right_frame, text='Exclude Movie: ', font='Calibri 14')
excluded_items_label.pack(expand=True, fill='x')

excluded_items_table = ttk.Treeview(second_right_frame, columns=('item'), show='tree')

excluded_items_remove_button = ttk.Button(excluded_items_label, text='Remove', command=lambda: remove_selected_item(excluded_items_table, excluded))
excluded_items_remove_button.pack(side='right', padx=5, pady=5)

excluded_items_add_button = ttk.Button(excluded_items_label, text='Add', command=lambda: add_item(excluded_items_table, excluded))
excluded_items_add_button.pack(side='right', padx=5, pady=5)

excluded_items_table.pack(expand=True, fill='x')


third_right_frame = ttk.Frame()
third_right_frame.grid(row=5, column=0, columnspan=2, sticky='nsew')




get_result_button = ttk.Button(third_right_frame, text='Get Result', command=show_result)
get_result_button.pack(padx=5, pady=10)



if __name__ == '__main__':
    root.mainloop()
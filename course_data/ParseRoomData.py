import sqlite3
conn = sqlite3.connect('database.db')


def main():
    c = conn.cursor()

    f = open("roomdata.txt", "r")
    data = f.readlines()
    for x in data:
        list1 = x.split(":")
        building = list1[0][0]
        floor = list1[0][1]
        room = list1[0][1:]

        coords = list1[1].split(", ")
        lat = float(coords[1])
        long = float(coords[2])
        print(building, floor, room)
        print(lat, long)

        insert_coords(lat, long)

        # TODO WIP
        c.execute('SELECT FROM coordinates WHERE lat=? AND long=?', lat, long)
        coord_id = c.fetchone()[0]

        c.execute('INSERT INTO location_table(?, ?, ?)', coord_id, building, room)
        conn.commit()


def insert_coords(lat, long):
    c = conn.cursor()
    c.execute('INSERT INTO coordinates (?, ?', lat, long)
    conn.commit()


if __name__ == "__main__":
    main()
    conn.close()

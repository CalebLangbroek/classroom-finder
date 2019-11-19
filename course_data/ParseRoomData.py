import sqlite3
conn = sqlite3.connect('database.db')


def main():
    c = conn.cursor()

    f = open("roomdata.txt", "r")
    data = f.readlines()

    delete_all()

    for x in data:
        list1 = x.split(":")
        building = list1[0][0]
        level = list1[0][1]
        room = list1[0][1:]

        coords = list1[1].split(", ")
        lat = float(coords[1])
        long = float(coords[2])
        print(building, level, room)
        print(lat, long)

        insert_coords(lat, long)

        c.execute('SELECT id FROM coordinates WHERE lat=? AND long=?', (lat, long))
        coord_id = c.fetchone()[0]
        print(coord_id)
        if coord_id is not None:
            c.execute('INSERT INTO rooms(coor_id, room, building, level) VALUES (?, ?, ?, ?)',
                      (coord_id, room, building, level))
            conn.commit()


# Insert coords into coordinates
def insert_coords(lat, long):
    c = conn.cursor()

    # Only inserts unique values
    c.execute('SELECT * FROM coordinates WHERE (lat=? AND long=?)', (lat, long))
    entry = c.fetchone()
    if entry is None:
        c.execute('INSERT INTO coordinates(lat, long) VALUES (?, ?)', (lat, long))

    conn.commit()


# Wipes tables + counters. Used so db is clean when starting script
def delete_all():
    c = conn.cursor()
    c.execute('DELETE FROM coordinates')
    c.execute('DELETE FROM rooms')
    c.execute('UPDATE sqlite_sequence SET seq = ? WHERE name = ? or name = ?', (0, 'coordinates', 'rooms'))
    conn.commit()


if __name__ == "__main__":
    main()
    conn.close()

import sqlite3
import math

conn = sqlite3.connect('database.db')


def main():
    c = conn.cursor()

    f = open("room_data.txt", "r")
    data = f.readlines()
    header = ""
    delete_all()

    # Insert all coords into coordinates table
    for x in data:
        if "Path Coordinates" in x:
            header = "Path Coordinates"
        elif x == "\n":
            header = ""
        elif "Path Coordinates" in header:
            insert_coords(x.upper().replace('\n', ''))

    # Insert all neighbours into neighbours table
    for x in data:
        if "Path Neighbours" in x:
            header = "Path Neighbours"
        elif x == "\n":
            header = ""
        elif header == "Path Neighbours":
            insert_neighbours(x.upper().replace('\n', ''))

    # Insert all rooms into room table
    for x in data:
        if "Classroom Coordinates" in x:
            header = "Classroom Coordinates"
        elif x == "\n":
            header = ""
        elif header == "Classroom Coordinates":
            insert_rooms(x.upper().replace('\n', ''))

    conn.close()


# Insert coords into coordinates
def insert_coords(line):
    c = conn.cursor()

    data = line.split(": ")

    coord_id = data[0]

    coords = data[1].split(", ")
    lat = float(coords[0])
    long = float(coords[1])

    print(coord_id)
    print(lat, long)

    # Only inserts unique values
    c.execute('SELECT id FROM coordinates WHERE id=?', (coord_id, ))
    entry = c.fetchone()
    if entry is None:
        c.execute('INSERT INTO coordinates(id, lat, long) VALUES (?, ?, ?)', (coord_id, lat, long))

    conn.commit()


def insert_neighbours(line):
    c = conn.cursor()

    data = line.split(": ")
    from_coord_id = data[0]
    to_coord_ids = data[1].split(", ")

    c.execute('SELECT lat, long FROM coordinates WHERE id=?', (from_coord_id,))
    from_latlong = c.fetchone()
    if from_latlong is None:
        print(from_coord_id, 'is not a coordinate')
    else:
        from_lat = from_latlong[0]
        from_long = from_latlong[1]
        print(from_coord_id, ': ', from_lat, from_long)

    for to_coord_id in to_coord_ids:
        c.execute('SELECT lat, long FROM coordinates WHERE id=?', (to_coord_id,))
        to_latlong = c.fetchone()
        if to_latlong is None:
            print(to_coord_id + ': is not a coordinate')
        else:
            to_lat = to_latlong[0]
            to_long = to_latlong[1]

        if from_latlong is not None and to_latlong is not None:
            x_dist = abs(abs(from_long) - abs(to_long))
            y_dist = abs(abs(from_lat) - abs(to_lat))
            dist = math.sqrt( x_dist * x_dist + y_dist * y_dist)

            c.execute('INSERT INTO reachable_coordinates(from_coor_id, to_coor_id, cost) VALUES (?, ?, ?)',
                      (from_coord_id, to_coord_id, dist))

    conn.commit()


def insert_rooms(line):
    c = conn.cursor()

    data = line.split(": ")
    room_id = data[0]
    coord_id = data[1]
    building = room_id[0:1]
    level = room_id[1:2]
    c.execute('INSERT INTO rooms(id, coor_id, building, level) VALUES (?, ?, ?, ?)', (room_id, coord_id, building, level))
    conn.commit()
    print(room_id + coord_id + building + level)


# Wipes tables + counters. Used so db is clean when starting script
def delete_all():
    c = conn.cursor()
    c.execute('DELETE FROM rooms')
    c.execute('DELETE FROM reachable_coordinates')
    c.execute('DELETE FROM coordinates')
    c.execute('UPDATE sqlite_sequence SET seq = ? WHERE name = ?', (0, 'reachable_coordinates',))
    conn.commit()


if __name__ == "__main__":
    main()

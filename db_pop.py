import mysql.connector
from decimal import Decimal

DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "password",
    "database": "bookstore"
}

books = [
    ("The Hobbit", "J.R.R. Tolkien", Decimal("2.99"), Decimal("9.99")),
    ("The Lord of the Rings", "J.R.R. Tolkien", Decimal("3.49"), Decimal("14.99")),
    ("1984", "George Orwell", Decimal("1.99"), Decimal("8.49")),
    ("To Kill a Mockingbird", "Harper Lee", Decimal("2.29"), Decimal("10.99")),
    ("Pride and Prejudice", "Jane Austen", Decimal("1.49"), Decimal("7.99")),
    ("The Great Gatsby", "F. Scott Fitzgerald", Decimal("2.19"), Decimal("9.49")),
    ("Moby Dick", "Herman Melville", Decimal("2.59"), Decimal("11.99")),
    ("War and Peace", "Leo Tolstoy", Decimal("3.99"), Decimal("15.99")),
    ("Crime and Punishment", "Fyodor Dostoevsky", Decimal("2.79"), Decimal("12.49")),
    ("The Catcher in the Rye", "J.D. Salinger", Decimal("1.99"), Decimal("8.99")),
    ("Brave New World", "Aldous Huxley", Decimal("2.49"), Decimal("9.99")),
    ("Animal Farm", "George Orwell", Decimal("1.49"), Decimal("7.49")),
    ("The Chronicles of Narnia", "C.S. Lewis", Decimal("3.29"), Decimal("13.99")),
    ("The Alchemist", "Paulo Coelho", Decimal("2.39"), Decimal("10.49")),
    ("Jane Eyre", "Charlotte Brontë", Decimal("2.59"), Decimal("11.49")),
    ("The Odyssey", "Homer", Decimal("2.99"), Decimal("9.99")),
    ("Frankenstein", "Mary Shelley", Decimal("1.89"), Decimal("8.99")),
    ("Wuthering Heights", "Emily Brontë", Decimal("2.29"), Decimal("9.49")),
    ("Dracula", "Bram Stoker", Decimal("2.49"), Decimal("9.99")),
    ("Les Misérables", "Victor Hugo", Decimal("3.99"), Decimal("16.49"))
]

try:
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()

    insert_query = """
        INSERT INTO books (title, author, rent_price, buy_price, is_rented)
        VALUES (%s, %s, %s, %s, %s)
    """

    for book in books:
        cursor.execute(insert_query, (*book, False))

    conn.commit()
    print(f"Inserted {cursor.rowcount} books successfully")

except mysql.connector.Error as err:
    print(f"Error: {err}")

finally:
    if cursor:
        cursor.close()
    if conn:
        conn.close()

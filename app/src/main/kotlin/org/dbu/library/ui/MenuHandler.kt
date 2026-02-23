package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "7" -> {
            listAllPatrons(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    print("Enter ISBN: ")
    val isbn = readlnOrNull() ?: return
    print("Enter Title: ")
    val title = readlnOrNull() ?: return
    print("Enter Author: ")
    val author = readlnOrNull() ?: return
    print("Enter Year: ")
    val yearStr = readlnOrNull() ?: return
    val year = yearStr.toIntOrNull() ?: 0

    val book = Book(isbn, title, author, year)
    val success = service.addBook(book)
    if (success) {
        println("Book added successfully.")
    } else {
        println("Book with this ISBN already exists.")
    }
}

fun registerPatron(repository: LibraryRepository) {
    print("Enter Patron ID: ")
    val id = readlnOrNull() ?: return
    print("Enter Name: ")
    val name = readlnOrNull() ?: return
    print("Enter Email: ")
    val email = readlnOrNull() ?: return
    print("Enter Phone: ")
    val phone = readlnOrNull() ?: return

    val patron = Patron(id, name, email, phone)
    val success = repository.addPatron(patron)
    if (success) {
        println("Patron registered successfully.")
    } else {
        println("Patron with this ID already exists.")
    }
}

fun borrowBook(service: LibraryService) {
    print("Enter Patron ID: ")
    val patronId = readlnOrNull() ?: return
    print("Enter ISBN: ")
    val isbn = readlnOrNull() ?: return

    val result = service.borrowBook(patronId, isbn)
    when (result) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully.")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is not currently available.")
        BorrowResult.LIMIT_REACHED -> println("Patron has reached the borrowing limit.")
    }
}

fun returnBook(service: LibraryService) {
    print("Enter Patron ID: ")
    val patronId = readlnOrNull() ?: return
    print("Enter ISBN: ")
    val isbn = readlnOrNull() ?: return

    val success = service.returnBook(patronId, isbn)
    if (success) {
        println("Book returned successfully.")
    } else {
        println("Failed to return book.")
    }
}

fun search(service: LibraryService) {
    print("Enter search query: ")
    val query = readlnOrNull() ?: return
    val results = service.search(query)
    
    if (results.isEmpty()) {
        println("No books found matching the query.")
    } else {
        for (book in results) {
            println("- ${book.title} by ${book.author} [${if (book.isAvailable) "Available" else "Borrowed"}]")
        }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("Library is empty.")
    } else {
        for (book in books) {
            println("- ${book.title} by ${book.author} (ISBN: ${book.isbn}) [${if (book.isAvailable) "Available" else "Borrowed"}]")
        }
    }
}
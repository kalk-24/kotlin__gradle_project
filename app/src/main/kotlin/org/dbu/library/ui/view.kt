package org.dbu.library.ui

import org.dbu.library.repository.LibraryRepository

fun listAllPatrons(repository: LibraryRepository) {
    val patrons = repository.getAllPatrons()
    if (patrons.isEmpty()) {
        println("No patrons registered.")
    } else {
        for (patron in patrons) {
            println("- ${patron.name} (ID: ${patron.id}, Email: ${patron.email}, Phone: ${patron.phone})")
            if (patron.borrowedBooks.isEmpty()) {
                println("  No books borrowed.")
            } else {
                val borrowedTitles = patron.borrowedBooks.map { isbn ->
                    val book = repository.findBook(isbn)
                    book?.title ?: "Unknown Book (ISBN: $isbn)"
                }
                println("  Borrowed books: ${borrowedTitles.joinToString(", ")}")
            }
        }
    }
}

#include <iostream>
#include "list.h"

List::List() {
	first = nullptr;
}

List::~List() {
	Node* current = first;
	while (current != nullptr) {
		Node* temp = current->next;
		delete current;
		current = temp;
	}
}

bool List::exists(int d) const {
	Node* current = first;
	while (current != nullptr) {
		if (current->value == d) {
			return true;
		}
		current = current->next;
	}
	return false;
}

int List::size() const {
	int size = 0;
	Node* c = first;
	while (c != nullptr) {
		c = c->next;
		size++;
	}
	return size;
}

bool List::empty() const {
	/*
	 * Alternativt kan man skriva
	 * return first == nullptr
	 */
	if (first == nullptr) {
		return true;
	} else {
		return false;
	}
}

void List::insertFirst(int d) {
	first = new Node(d, first);
}

void List::remove(int d, DeleteFlag df) {
	Node* current = first;
	Node* prev = first;
	while (current != nullptr) {
		if ((df == DeleteFlag::EQUAL && d == current->value) ||
				(df == DeleteFlag::LESS && d > current->value) ||
				(df == DeleteFlag::GREATER && d < current->value)) {
			if (prev == first) {
				first = first->next;
			}
			
			prev->next = current->next;
			delete current;
			return;
			
		}
		prev = current;
		current = current->next;
	}
}

void List::print() const {
	Node* c = first;
	while (c != nullptr) {
		std::cout << c->value << " ";
		c = c->next;
	}
	std::cout << std::endl;
}

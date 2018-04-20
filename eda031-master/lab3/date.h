#ifndef DATE_H
#define DATE_H

#include <istream>
#include <ostream>

class Date {
public:
	Date();                    // today's date
	Date(int y, int m, int d); // yyyy-mm-dd
	int getYear() const;       // get the year
	int getMonth() const;      // get the month
	int getDay() const;        // get the day
	void setDate(int y, int m, int d);
	void next();               // advance to next day

	friend std::istream& operator>>(std::istream& stream, Date& d);
	friend std::ostream& operator<<(std::ostream& stream, const Date& d);
private:
	int year;  // the year (four digits)
	int month; // the month (1-12)
	int day;   // the day (1-..)
	static int daysPerMonth[12]; // number of days in each month
};



#endif

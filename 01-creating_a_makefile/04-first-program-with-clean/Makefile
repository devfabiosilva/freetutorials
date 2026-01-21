
MYPROGNAME=myprogram

all:
	@echo "Building program $(MYPROGNAME) ..."
	@gcc -O2 src/main.c -o $(MYPROGNAME)
	@echo "Building $(MYPROGNAME) success"

.PHONY: clean
clean:
	@echo "Cleaning $(MYPROGNAME)"
	@rm $(MYPROGNAME) -v
	@echo "$(MYPROGNAME) removed"


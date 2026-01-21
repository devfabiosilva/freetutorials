
MYPROGNAME=myprogram

all:main

main:
	@echo "Building program $(MYPROGNAME) ..."
	@gcc -O2 src/main.c -o $(MYPROGNAME)
	@echo "Building $(MYPROGNAME) success"

.PHONY: clean buildAndRun
clean:
	@echo "Cleaning $(MYPROGNAME)"
	@rm $(MYPROGNAME) -v
	@echo "$(MYPROGNAME) removed"

buildAndRun: main
	@echo "Running $(MYPROGNAME) ..."
	./$(MYPROGNAME)


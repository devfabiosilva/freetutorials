#export LD_LIBRARY_PATH
INCLUDEDIR=$(PWD)/include
MYPROGNAME=myprogram
LIBNAME=basicCalc
SRCLIB=$(PWD)/src/lib

all:main

$(LIBNAME):
	@echo "Building dependency library $(LIBNAME) ..."
	@gcc -O2 -shared -fPIC -o lib$(LIBNAME).so $(SRCLIB)/$(LIBNAME).c

main: $(LIBNAME)
	@echo "Building program $(MYPROGNAME) ..."
	@gcc -O2 src/main.c -L$(PWD) -l$(LIBNAME) -I$(INCLUDEDIR) -o $(MYPROGNAME)
	@echo "Building $(MYPROGNAME) success"

.PHONY: clean buildAndRun
clean:
	@echo "Cleaning $(MYPROGNAME)"
	@rm $(MYPROGNAME) -v
	@echo "$(MYPROGNAME) removed"
	@echo "Cleaning lib$(LIBNAME).so ..."
	@rm lib$(LIBNAME).so -v
	@echo "lib$(LIBNAME).so removed"

buildAndRun: main
	@echo "Running $(MYPROGNAME) ..."
	./$(MYPROGNAME)


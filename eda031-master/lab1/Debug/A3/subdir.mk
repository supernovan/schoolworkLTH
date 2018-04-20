################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CC_SRCS += \
../A3/hello.cc \
../A3/list.cc \
../A3/ltest.cc 

O_SRCS += \
../A3/hello.o \
../A3/list.o \
../A3/ltest.o 

CC_DEPS += \
./A3/hello.d \
./A3/list.d \
./A3/ltest.d 

OBJS += \
./A3/hello.o \
./A3/list.o \
./A3/ltest.o 


# Each subdirectory must supply rules for building sources it contributes
A3/%.o: ../A3/%.cc
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '



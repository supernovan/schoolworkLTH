################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CC_SRCS += \
../A5/coding.cc \
../A5/decode.cc \
../A5/encode.cc 

CC_DEPS += \
./A5/coding.d \
./A5/decode.d \
./A5/encode.d 

OBJS += \
./A5/coding.o \
./A5/decode.o \
./A5/encode.o 


# Each subdirectory must supply rules for building sources it contributes
A5/%.o: ../A5/%.cc
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

A5/decode.o: ../A5/decode.cc
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -std=c++11 -MMD -MP -MF"$(@:%.o=%.d)" -MT"A5/decode.d" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '



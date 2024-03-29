// -*- C++ -*-
//
// $Id: sensorSimulatorS.cpp,v 1.1 2009/11/08 19:23:13 almamgr Exp $

// ****  Code generated by the The ACE ORB (TAO) IDL Compiler v1.6.5 ****
// TAO and the TAO IDL Compiler have been developed by:
//       Center for Distributed Object Computing
//       Washington University
//       St. Louis, MO
//       USA
//       http://www.cs.wustl.edu/~schmidt/doc-center.html
// and
//       Distributed Object Computing Laboratory
//       University of California at Irvine
//       Irvine, CA
//       USA
//       http://doc.ece.uci.edu/
// and
//       Institute for Software Integrated Systems
//       Vanderbilt University
//       Nashville, TN
//       USA
//       http://www.isis.vanderbilt.edu/
//
// Information about TAO is available at:
//     http://www.cs.wustl.edu/~schmidt/TAO.html


// TAO_IDL - Generated from 
// be/be_codegen.cpp:703

#ifndef _TAO_IDL____OBJECT_SENSORSIMULATORS_CPP_
#define _TAO_IDL____OBJECT_SENSORSIMULATORS_CPP_


#include "sensorSimulatorS.h"
#include "tao/PortableServer/Operation_Table_Perfect_Hash.h"
#include "tao/PortableServer/Upcall_Command.h"
#include "tao/PortableServer/Upcall_Wrapper.h"
#include "tao/TAO_Server_Request.h"
#include "tao/ORB_Core.h"
#include "tao/Profile.h"
#include "tao/Stub.h"
#include "tao/IFR_Client_Adapter.h"
#include "tao/Object_T.h"
#include "tao/AnyTypeCode/TypeCode.h"
#include "tao/AnyTypeCode/DynamicC.h"
#include "tao/CDR.h"
#include "tao/operation_details.h"
#include "tao/PortableInterceptor.h"
#include "tao/PortableServer/Basic_SArguments.h"
#include "tao/PortableServer/Object_SArgument_T.h"
#include "tao/PortableServer/Special_Basic_SArguments.h"
#include "tao/PortableServer/UB_String_SArguments.h"
#include "tao/PortableServer/TypeCode_SArg_Traits.h"
#include "tao/PortableServer/Object_SArg_Traits.h"
#include "tao/PortableServer/get_arg.h"
#include "tao/Special_Basic_Arguments.h"
#include "tao/UB_String_Arguments.h"
#include "tao/Basic_Arguments.h"
#include "ace/Dynamic_Service.h"
#include "ace/Malloc_Allocator.h"

#if !defined (__ACE_INLINE__)
#include "sensorSimulatorS.inl"
#endif /* !defined INLINE */

// TAO_IDL - Generated from
// be/be_visitor_arg_traits.cpp:73

TAO_BEGIN_VERSIONED_NAMESPACE_DECL


// Arg traits specializations.
namespace TAO
{
}

TAO_END_VERSIONED_NAMESPACE_DECL



// TAO_IDL - Generated from
// be/be_visitor_arg_traits.cpp:73

TAO_BEGIN_VERSIONED_NAMESPACE_DECL


// Arg traits specializations.
namespace TAO
{
}

TAO_END_VERSIONED_NAMESPACE_DECL



// TAO_IDL - Generated from
// be/be_interface.cpp:1555

class TAO_SS_SensorSimulator_Perfect_Hash_OpTable
  : public TAO_Perfect_Hash_OpTable
{
private:
  unsigned int hash (const char *str, unsigned int len);

public:
  const TAO_operation_db_entry * lookup (const char *str, unsigned int len);
};

/* C++ code produced by gperf version 2.8 (ACE version) */
/* Command-line: /alma/ACS-8.1/TAO/ACE_wrappers/build/linux/bin/gperf -m -M -J -c -C -D -E -T -f 0 -F 0,0 -a -o -t -p -K opname -L C++ -Z TAO_SS_SensorSimulator_Perfect_Hash_OpTable -N lookup  */
unsigned int
TAO_SS_SensorSimulator_Perfect_Hash_OpTable::hash (const char *str, unsigned int len)
{
  static const unsigned char asso_values[] =
    {
#if defined (ACE_MVS)
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39,  0,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39,  0,
     39, 15, 15,  0,  0,  0, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39,  0, 39,  0,
      0, 39, 39,  0, 39, 39, 39, 39, 39, 39,
     39, 39, 15,  5, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39,
#else
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39, 39, 39,
     39, 39, 39, 39, 39,  0, 39,  0, 39, 15,
     15,  0,  0,  0, 39, 39, 39, 39,  0, 39,
      0,  0, 39, 39,  0, 15,  5, 39, 39, 39,
     39, 39, 39, 39, 39, 39, 39, 39,
#endif /* ACE_MVS */
    };
  return len + asso_values[(int) str[len - 1]] + asso_values[(int) str[0]];
}

const TAO_operation_db_entry *
TAO_SS_SensorSimulator_Perfect_Hash_OpTable::lookup (const char *str, unsigned int len)
{
  enum
    {
      TOTAL_KEYWORDS = 15,
      MIN_WORD_LENGTH = 2,
      MAX_WORD_LENGTH = 26,
      MIN_HASH_VALUE = 2,
      MAX_HASH_VALUE = 38,
      HASH_VALUE_RANGE = 37,
      DUPLICATES = 0,
      WORDLIST_SIZE = 17
    };

  static const TAO_operation_db_entry  wordlist[] =
    {
      {"",0,0},{"",0,0},
      {"on", &POA_SS::SensorSimulator::on_skel, 0},
      {"off", &POA_SS::SensorSimulator::off_skel, 0},
      {"",0,0},
      {"_is_a", &POA_SS::SensorSimulator::_is_a_skel, 0},
      {"",0,0},{"",0,0},
      {"resetAll", &POA_SS::SensorSimulator::resetAll_skel, 0},
      {"_get_name", &POA_SS::SensorSimulator::_get_name_skel, 0},
      {"_interface", &POA_SS::SensorSimulator::_interface_skel, 0},
      {"",0,0},{"",0,0},{"",0,0},{"",0,0},
      {"_component", &POA_SS::SensorSimulator::_component_skel, 0},
      {"",0,0},{"",0,0},
      {"_non_existent", &POA_SS::SensorSimulator::_non_existent_skel, 0},
      {"_get_componentState", &POA_SS::SensorSimulator::_get_componentState_skel, 0},
      {"",0,0},{"",0,0},{"",0,0},{"",0,0},
      {"getStatus", &POA_SS::SensorSimulator::getStatus_skel, 0},
      {"descriptor", &POA_SS::SensorSimulator::descriptor_skel, 0},
      {"get_characteristic_by_name", &POA_SS::SensorSimulator::get_characteristic_by_name_skel, 0},
      {"",0,0},{"",0,0},
      {"_repository_id", &POA_SS::SensorSimulator::_repository_id_skel, 0},
      {"",0,0},{"",0,0},{"",0,0},{"",0,0},
      {"find_characteristic", &POA_SS::SensorSimulator::find_characteristic_skel, 0},
      {"",0,0},{"",0,0},{"",0,0},
      {"get_all_characteristics", &POA_SS::SensorSimulator::get_all_characteristics_skel, 0},
    };

  if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH)
    {
      unsigned int key = hash (str, len);

      if (key <= MAX_HASH_VALUE && key >= MIN_HASH_VALUE)
        {
          const char *s = wordlist[key].opname;

          if (*str == *s && !strncmp (str + 1, s + 1, len - 1))
            return &wordlist[key];
        }
    }
  return 0;
}

static TAO_SS_SensorSimulator_Perfect_Hash_OpTable tao_SS_SensorSimulator_optable;

// TAO_IDL - Generated from
// be/be_visitor_interface/interface_ss.cpp:984

TAO::Collocation_Proxy_Broker *
SS__TAO_SensorSimulator_Proxy_Broker_Factory_function ( ::CORBA::Object_ptr)
{
  return reinterpret_cast<TAO::Collocation_Proxy_Broker *> (0xdead); // Dummy
}

int
SS__TAO_SensorSimulator_Proxy_Broker_Factory_Initializer (size_t)
{
  SS__TAO_SensorSimulator_Proxy_Broker_Factory_function_pointer = 
    SS__TAO_SensorSimulator_Proxy_Broker_Factory_function;
  
  return 0;
}

static int
SS__TAO_SensorSimulator_Proxy_Broker_Stub_Factory_Initializer_Scarecrow =
  SS__TAO_SensorSimulator_Proxy_Broker_Factory_Initializer (
      reinterpret_cast<size_t> (SS__TAO_SensorSimulator_Proxy_Broker_Factory_Initializer)
    );

// TAO_IDL - Generated from 
// be/be_visitor_interface/interface_ss.cpp:103

POA_SS::SensorSimulator::SensorSimulator (void)
  : TAO_ServantBase ()
{
  this->optable_ = &tao_SS_SensorSimulator_optable;
}

POA_SS::SensorSimulator::SensorSimulator (const SensorSimulator& rhs)
  : TAO_Abstract_ServantBase (rhs),
    TAO_ServantBase (rhs),
    POA_ACS::ACSComponent (rhs),
    POA_ACS::CharacteristicModel (rhs),
    POA_ACS::CharacteristicComponent (rhs)
{
}

POA_SS::SensorSimulator::~SensorSimulator (void)
{
}
namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class on_SensorSimulator
    : public TAO::Upcall_Command
  {
  public:
    inline on_SensorSimulator (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::CORBA::Long>::in_arg_type arg_1 =
        TAO::Portable_Server::get_in_arg< ::CORBA::Long> (
          this->operation_details_,
          this->args_,
          1);
        
      this->servant_->on (
        arg_1);
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


// TAO_IDL - Generated from 
// be/be_visitor_operation/operation_ss.cpp:190

void POA_SS::SensorSimulator::on_skel (
    TAO_ServerRequest & server_request,
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< void>::ret_val retval;
  TAO::SArg_Traits< ::CORBA::Long>::in_arg_val _tao_sensorID;
  
  TAO::Argument * const args[] =
    {
      &retval,
      &_tao_sensorID
    };
  
  static size_t const nargs = 2;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);

  on_SensorSimulator command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class off_SensorSimulator
    : public TAO::Upcall_Command
  {
  public:
    inline off_SensorSimulator (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::CORBA::Long>::in_arg_type arg_1 =
        TAO::Portable_Server::get_in_arg< ::CORBA::Long> (
          this->operation_details_,
          this->args_,
          1);
        
      this->servant_->off (
        arg_1);
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


// TAO_IDL - Generated from 
// be/be_visitor_operation/operation_ss.cpp:190

void POA_SS::SensorSimulator::off_skel (
    TAO_ServerRequest & server_request,
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< void>::ret_val retval;
  TAO::SArg_Traits< ::CORBA::Long>::in_arg_val _tao_sensorID;
  
  TAO::Argument * const args[] =
    {
      &retval,
      &_tao_sensorID
    };
  
  static size_t const nargs = 2;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);

  off_SensorSimulator command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class resetAll_SensorSimulator
    : public TAO::Upcall_Command
  {
  public:
    inline resetAll_SensorSimulator (
      POA_SS::SensorSimulator * servant)
      : servant_ (servant)
    {
    }
    
    virtual void execute (void)
    {
      this->servant_->resetAll ();
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
  };
}


// TAO_IDL - Generated from 
// be/be_visitor_operation/operation_ss.cpp:190

void POA_SS::SensorSimulator::resetAll_skel (
    TAO_ServerRequest & server_request,
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< void>::ret_val retval;
  
  TAO::Argument * const args[] =
    {
      &retval
    };
  
  static size_t const nargs = 1;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);

  resetAll_SensorSimulator command (
    impl);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class getStatus_SensorSimulator
    : public TAO::Upcall_Command
  {
  public:
    inline getStatus_SensorSimulator (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::CORBA::Long>::ret_arg_type retval =
        TAO::Portable_Server::get_ret_arg< ::CORBA::Long> (
          this->operation_details_,
          this->args_);
      
      TAO::SArg_Traits< ::CORBA::Long>::in_arg_type arg_1 =
        TAO::Portable_Server::get_in_arg< ::CORBA::Long> (
          this->operation_details_,
          this->args_,
          1);
        
      retval =
        this->servant_->getStatus (
          arg_1);
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


// TAO_IDL - Generated from 
// be/be_visitor_operation/operation_ss.cpp:190

void POA_SS::SensorSimulator::getStatus_skel (
    TAO_ServerRequest & server_request,
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< ::CORBA::Long>::ret_val retval;
  TAO::SArg_Traits< ::CORBA::Long>::in_arg_val _tao_sensorID;
  
  TAO::Argument * const args[] =
    {
      &retval,
      &_tao_sensorID
    };
  
  static size_t const nargs = 2;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);

  getStatus_SensorSimulator command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}



// TAO_IDL - Generated from 
// be/be_visitor_interface/interface_ss.cpp:169

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class _is_a_SensorSimulator_Upcall_Command
    : public TAO::Upcall_Command
  {
  public:
    inline _is_a_SensorSimulator_Upcall_Command (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::ACE_InputCDR::to_boolean>::ret_arg_type retval =
        TAO::Portable_Server::get_ret_arg< ::ACE_InputCDR::to_boolean> (
          this->operation_details_,
          this->args_);
      
      TAO::SArg_Traits< ::CORBA::Char *>::in_arg_type arg_1 =
        TAO::Portable_Server::get_in_arg< ::CORBA::Char *> (
          this->operation_details_,
          this->args_,
          1);
        
      retval =
        this->servant_-> _is_a (
          arg_1);
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


void POA_SS::SensorSimulator::_is_a_skel (
    TAO_ServerRequest & server_request, 
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< ::ACE_InputCDR::to_boolean>::ret_val retval;
  TAO::SArg_Traits< ::CORBA::Char *>::in_arg_val _tao_repository_id;
  
  TAO::Argument * const args[] =
    {
      &retval,
      &_tao_repository_id
    };
  
  static size_t const nargs = 2;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);
  
  _is_a_SensorSimulator_Upcall_Command command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class _non_existent_SensorSimulator_Upcall_Command
    : public TAO::Upcall_Command
  {
  public:
    inline _non_existent_SensorSimulator_Upcall_Command (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::ACE_InputCDR::to_boolean>::ret_arg_type retval =
        TAO::Portable_Server::get_ret_arg< ::ACE_InputCDR::to_boolean> (
          this->operation_details_,
          this->args_);
      
      retval =
        this->servant_-> _non_existent ();
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


void POA_SS::SensorSimulator::_non_existent_skel (
    TAO_ServerRequest & server_request, 
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< ::ACE_InputCDR::to_boolean>::ret_val retval;
  
  TAO::Argument * const args[] =
    {
      &retval
    };
  
  static size_t const nargs = 1;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);
  
  _non_existent_SensorSimulator_Upcall_Command command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}
namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class _repository_id_SensorSimulator_Upcall_Command
    : public TAO::Upcall_Command
  {
  public:
    inline _repository_id_SensorSimulator_Upcall_Command (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::CORBA::Char *>::ret_arg_type retval =
        TAO::Portable_Server::get_ret_arg< ::CORBA::Char *> (
          this->operation_details_,
          this->args_);
      
      retval =
        this->servant_-> _repository_id ();
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


void POA_SS::SensorSimulator::_repository_id_skel (
    TAO_ServerRequest & server_request, 
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant)
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< ::CORBA::Char *>::ret_val retval;
  
  TAO::Argument * const args[] =
    {
      &retval
    };
  
  static size_t const nargs = 1;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);
  
  _repository_id_SensorSimulator_Upcall_Command command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

// TAO_IDL - Generated from 
// be/be_visitor_interface/interface_ss.cpp:508

void POA_SS::SensorSimulator::_interface_skel (
    TAO_ServerRequest & server_request, 
    void * /* servant_upcall */,
    void * servant)
{
  TAO_IFR_Client_Adapter *_tao_adapter =
    ACE_Dynamic_Service<TAO_IFR_Client_Adapter>::instance (
        TAO_ORB_Core::ifr_client_adapter_name ()
      );
    
  if (_tao_adapter == 0)
    {
      throw ::CORBA::INTF_REPOS (::CORBA::OMGVMCID | 1, ::CORBA::COMPLETED_NO);
    }
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);
  ::CORBA::InterfaceDef_ptr _tao_retval = impl->_get_interface ();
  server_request.init_reply ();
  TAO_OutputCDR &_tao_out = *server_request.outgoing ();
  
  ::CORBA::Boolean const _tao_result =
    _tao_adapter->interfacedef_cdr_insert (_tao_out, _tao_retval);
  
  _tao_adapter->dispose (_tao_retval);
  
  if (_tao_result == false)
    {
      throw ::CORBA::MARSHAL ();
    }
}

namespace POA_SS
{
  
  
  // TAO_IDL - Generated from
  // be/be_visitor_operation/upcall_command_ss.cpp:136
  
  class _get_component_SensorSimulator_Upcall_Command
    : public TAO::Upcall_Command
  {
  public:
    inline _get_component_SensorSimulator_Upcall_Command (
      POA_SS::SensorSimulator * servant,
      TAO_Operation_Details const * operation_details,
      TAO::Argument * const args[])
      : servant_ (servant)
        , operation_details_ (operation_details)
        , args_ (args)
    {
    }
    
    virtual void execute (void)
    {
      TAO::SArg_Traits< ::CORBA::Object>::ret_arg_type retval =
        TAO::Portable_Server::get_ret_arg< ::CORBA::Object> (
          this->operation_details_,
          this->args_);
      
      retval =
        this->servant_-> _get_component ();
    }
  
  private:
    POA_SS::SensorSimulator * const servant_;
    TAO_Operation_Details const * const operation_details_;
    TAO::Argument * const * const args_;
  };
}


void POA_SS::SensorSimulator::_component_skel (
    TAO_ServerRequest & server_request, 
    void * TAO_INTERCEPTOR (servant_upcall),
    void * servant
  )
{
#if TAO_HAS_INTERCEPTORS == 1
  static ::CORBA::TypeCode_ptr const * const exceptions = 0;
  static ::CORBA::ULong const nexceptions = 0;
#endif /* TAO_HAS_INTERCEPTORS */
  
  TAO::SArg_Traits< ::CORBA::Object>::ret_val retval;
  
  TAO::Argument * const args[] =
    {
      &retval
    };
  
  static size_t const nargs = 1;
  
  POA_SS::SensorSimulator * const impl =
    static_cast<POA_SS::SensorSimulator *> (servant);
  
  _get_component_SensorSimulator_Upcall_Command command (
    impl,
    server_request.operation_details (),
    args);
  
  TAO::Upcall_Wrapper upcall_wrapper;
  upcall_wrapper.upcall (server_request
                         , args
                         , nargs
                         , command
#if TAO_HAS_INTERCEPTORS == 1
                         , servant_upcall
                         , exceptions
                         , nexceptions
#endif  /* TAO_HAS_INTERCEPTORS == 1 */
                         );
}

::CORBA::Boolean POA_SS::SensorSimulator::_is_a (const char* value)
{
  return
    (
      !ACE_OS::strcmp (
          value,
          "IDL:alma/ACS/ACSComponent:1.0"
        ) ||
      !ACE_OS::strcmp (
          value,
          "IDL:alma/ACS/CharacteristicModel:1.0"
        ) ||
      !ACE_OS::strcmp (
          value,
          "IDL:alma/ACS/CharacteristicComponent:1.0"
        ) ||
      !ACE_OS::strcmp (
          value,
          "IDL:alma/SS/SensorSimulator:1.0"
        ) ||
      !ACE_OS::strcmp (
          value,
          "IDL:omg.org/CORBA/Object:1.0"
        )
    );
}

const char* POA_SS::SensorSimulator::_interface_repository_id (void) const
{
  return "IDL:alma/SS/SensorSimulator:1.0";
}

// TAO_IDL - Generated from
// be/be_visitor_interface/interface_ss.cpp:926

void POA_SS::SensorSimulator::_dispatch (TAO_ServerRequest & req, void * servant_upcall)
{
  this->synchronous_upcall_dispatch (req, servant_upcall, this);
}

// TAO_IDL - Generated from
// be/be_visitor_interface/interface_ss.cpp:852

SS::SensorSimulator *
POA_SS::SensorSimulator::_this (void)
{
  TAO_Stub *stub = this->_create_stub ();
  
  TAO_Stub_Auto_Ptr safe_stub (stub);
  ::CORBA::Object_ptr tmp = CORBA::Object_ptr ();
  
  ::CORBA::Boolean const _tao_opt_colloc =
    stub->servant_orb_var ()->orb_core ()->optimize_collocation_objects ();
  
  ACE_NEW_RETURN (
      tmp,
      ::CORBA::Object (stub, _tao_opt_colloc, this),
      0
    );
  
  ::CORBA::Object_var obj = tmp;
  (void) safe_stub.release ();
  
  typedef ::SS::SensorSimulator STUB_SCOPED_NAME;
  return
    TAO::Narrow_Utils<STUB_SCOPED_NAME>::unchecked_narrow (
        obj.in (),
        SS__TAO_SensorSimulator_Proxy_Broker_Factory_function_pointer
      );
}

#endif /* ifndef */


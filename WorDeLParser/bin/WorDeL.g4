// Define the grammar
grammar WorDeL;

options {
	language=Java;
}

@header {
	
    package generated;
	import representation.*;
	import representation.nodes.*;
	import representation.types.*;
	import representation.values.*;
	import support.*;
	import support.xml.*;
	import utilities.OpReader;
	
	import java.util.List;
	import java.util.Map;
	import java.util.HashMap;

}

// Declare class members
@members {

   	private Support support = new Support();
   	private Properties props;
    // read the initial set of operations (subject to change)
    {
    	support.initialize();
    	support.setOperators(OpReader.readOperators("Operators.txt"));
    }
}

/* In order to acomodate the return functionality for the parser rules, one has to declare 
 * the lexer tokens at the end of the grammar, after the definition of the parser rules
 */
/* ########################################## PARSER RULES ######################################### */

/* define a list of names */
/*name_list returns [List<String> list]
	: NAME COMMA nl=name_list {$list=nl.st; $list.add($NAME.text);}
	| NAME {$list=new ArrayList<String>(); $list.add($NAME.text);};*/
name_list returns [List<String> list]
		@init
		{
			$list=new ArrayList<String>();	
		}
		: NAME {$list.add($NAME.text.trim());} (COMMA NAME {$list.add($NAME.text.trim());} )* ;   // ALTERNATIVE rule- identical with the one above

/* ######################### TYPE DEFINITION ################################ */
tip :  'Integer' | 'String' | 'Float' | 'Boolean' |'File';
/* define the composite types */
composite_type returns [Type t]
		: tip {$t=BaseType.getInstance($tip.text);}
		| 'List' '<' composite_type '>' 
		 {$t=CompositeType.getInstance("List",$composite_type.t);}
		| 'Tuple' '(' composite_type (',' composite_type)* ')'
		 {$t=CompositeType.getInstance("Tuple",$composite_type.t);};

/* ######################### VARIABLE AND PORT DEFINITIONS #################### */
var_declaration returns [List<Port> multiPorts]
		@init
		{
			$multiPorts=new ArrayList<Port>();	 
		}
		:  name_list ':' composite_type 
		{for(String name:$name_list.list){$multiPorts.add(new Port(name,$composite_type.t));}};

/* list of ports declarations */
ports_declaration returns [List<Port> ports]
		@init
		{
			$ports=new ArrayList<Port>();	
		}
		: var=var_declaration {$ports.addAll($var.multiPorts);} 
		(',' var2=var_declaration  {$ports.addAll($var2.multiPorts);})* ;


/* ######################### VALUE SYSTEM DEFINITION ############################ */

int_value : DIGIT+;                          // one or more digits
string_value returns [String s]: '"'NAME'"' {$s=$NAME.text;};   // string values
float_value: int_value '.' int_value ;
boolean_value: 'true' | 'false' ;

value returns [Value val]
		: int_value {$val=support.extractValue($int_value.text,"integer");}
		| string_value {$val=support.extractValue($string_value.s,"string");}
		| boolean_value {$val=support.extractValue($boolean_value.text,"boolean");}
		| float_value {$val=support.extractValue($float_value.text,"float");}
		| list_value {$val=$list_value.lv;}
		| tuple_value {$val=$tuple_value.lv;} ;
		
list_value returns [ListValue lv]
			@init
			{
				$lv=new ListValue();
			}
			: '{' v1=value {$lv.add($v1.val);} (',' v2=value)* {$lv.add($v2.val);} '}';
			
tuple_value returns [ListValue lv]
			@init
			{
				$lv=new ListValue();
			}
			: '(' v1=value {$lv.add($v1.val);} (',' v2=value)* {$lv.add($v2.val);} ')';

		
value_list returns [List<Value> vl]
			@init
			{
				$vl=new ArrayList<Value>();
			}
		   : OPENPARAM v1=value {$vl.add($v1.val);} (COMMA v2=value {$vl.add($v2.val);})* CLOSEPARAM;	
		   		 
/* ########################### OPERAND INSTANTIATION ############################## */

/* ----------------------- auxiliaries ------------------------------------ */
instance_ports returns [List<String> list]	
		      : OPENPARAM in=name_list CLOSEPARAM {$list=$in.list;}  ;
		     
case_list returns [List<CaseItem> items] 
		 @init
		 {
		 	$items=new ArrayList<CaseItem>();
		 }
		 : i=case_item {$items.add($i.item);} (i2=case_item {$items.add($i2.item);})*;
		 
case_item returns [CaseItem item]
		  : val=value ':' OPENPARAM out=name_list CLOSEPARAM 
		  {$item=new CaseItem($val.val,$out.list);};
/*------------------------------------------------------------------------- */

operation returns [Node node] 
		 // declare the operator instantiation construct
		: in=instance_ports name=NAME ':' id=NAME out=instance_ports 
		 { $node=support.getOperationNode($name.text,$id.text,$in.list,$out.list);} 
		 
		 // declare the switch statement structure 
		| in=instance_ports  SWITCH '(' NAME ')'  cl=case_list 
		{ support.createSwitchNode($in.list,$NAME.text,$cl.items); }
		
		 // define the join statement structure
		| JOIN '(' NAME ')' instance_ports case_list;

/* list of operand instances */	 
operation_list : operation+ ;

/* ############################## TOP-LEVEL RULES ################################ */
/* top-level flow structure */
flow returns [FlowNode f]   
	:
	{
 	  support.initialize();
 	}
	FLOW COLON NAME
	'input:' in=ports_declaration {support.addPorts($in.ports,true);}
	'output:' out=ports_declaration {support.addPorts($out.ports,false);}
	operation_list 
	ENDFLOW 
	{ $f=support.buildFlow($NAME.text,$in.ports,$out.ports);};
		   
simulation returns [Simulation s]
		  : SIMULATION COLON sim_name=NAME 
		    value_list op_id=NAME COLON op_name=NAME instance_ports
		    ENDFLOW
		   {$s=support.buildSimulation($sim_name.text, $op_id.text, $op_name.text, $value_list.vl, $instance_ports.list );};
/* */ 
/* flow list */	  
flow_list returns [Map<String,FlowNode> flows]
		@init
		{
			$flows=new HashMap<String,FlowNode>();	
		}
		: f1=flow {$flows.put($f1.f.getId(),$f1.f);} 
		  (f2=flow {$flows.put($f2.f.getId(),$f2.f);})*;   // one or more flows*/

content returns [ContentNode c]
		@init
		{
			$c=new ContentNode();
		}
		: flow_list {$c.setFlows($flow_list.flows);} 
		(simulation {$c.setSimulation($simulation.s);})* EOF;   // one or more flows 
		 
/* ################################################################################################# */

/* ############################################  TOKENS  ########################################### */
NEWLINE : '\n' ;
//SPACE : ' ' ;
OPENPARAM : '[' ;
CLOSEPARAM : ']' ;
COMMA : ',' ;
COLON : ':' ;
LPAR : '(';
RPAR : ')';
FLOW : 'flow' ;
SIMULATION : 'simulation' ;
SWITCH: 'switch'; 
JOIN : 'join';  
ENDFLOW : 'end' ;

/** skip characters **/      
//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
fragment SPC : [' ' | '\t' | '\r' | '\f' | '\n'];//{$channel=HIDDEN;}; // ignore space, tabs, return, newline sequences
WS: SPC+ -> skip;
//' '+ ->skip {$channel=HIDDEN;}; //{' '+ | '\r'+ | '\n'+ | '\t'+} -> skip; // skip any number of space characters

/** define variable names **/
//NAME : [ a-z | A-Z | 0-9 ]+ ;              // any alphanumeric sequence defining a name
//NAME : [ a-z | A-Z ] [ a-z | A-Z | 0-9 ]+ ; // one lower or uppercase followed by at least one alphanumeric characters
NAME : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*;
DIGIT: '0'..'9';

/** comment definition **/
COMMENT: '//' .*? ('\n' | '\r')+ -> skip;   // line comment defined as anything between double-slash and newline char
MULTICOMMENT: '/*' .*? '*/' -> skip;  /* multiple line comment - skip anything between the '/*' token*/
/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Initial Developer of this code is David Baum.
 * Portions created by David Baum are Copyright (C) 1999 David Baum.
 * All Rights Reserved.
 *
 * Portions created by John Hansen are Copyright (C) 2005 John Hansen.
 * All Rights Reserved.
 *
 */

#ifndef __SRecord_h
#define __SRecord_h

#ifndef __PTypes_h
#include "PTypes.h"
#endif

#include <cstdio>

using std::FILE;

class SRecord
{
public:
					SRecord() : fLength(0), fStart(0), fData(0) {}
					~SRecord()	{ delete [] fData; }

	int				GetLength() const	{ return fLength; }
	const UByte*	GetData() const		{ return fData; }
	int				GetStart() const	{ return fStart; }

	bool			Read(FILE *fp, int maxLength);

	static int		ReadHexByte(const char *ptr);

private:
	int		fLength;
	int		fStart;
	UByte*	fData;
};

#endif

package com.ntukhpi.storozhuk.command;

import com.ntukhpi.storozhuk.context.Context;

public interface StateCommand {

    void execute(Context context, int index, String[] input);
}

#
# This file is part of the Raster Storage Archive (RSA).
#
# The RSA is free software: you can redistribute it and/or modify it under the
# terms of the GNU General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later
# version.
#
# The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
# A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# the RSA.  If not, see <http://www.gnu.org/licenses/>.
#
# Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
# http://www.crcsi.com.au/
#

#
# Provides bash completion for the RSA command line client. To enable it, run
# the following line before using rsacli:
#
#     . _rsacli_bash_completion.sh
#
# A good place to put that is in ~/.bashrc
#

# DATASET COMMANDS

function _rsa_list_datasets() {
    local executable
    executable="${COMP_WORDS[0]}"
    datasets=$(${executable} dataset search ${cur} 2>/dev/null | awk '{ print $2 }')
    #datasets=$(${executable} dataset search ${cur} | awk '{ print $2 }')
    COMPREPLY=( $(compgen -W "$datasets" -- ${cur}) )
    return 0
}

function _rsa_dataset() {
    local actions options action actiontoknum
    action="$1"
    actiontoknum="$2"

    #echo -e "\n$action@$actiontoknum"

    if [ $actiontoknum -eq $COMP_CWORD ]; then
        local actions
        actions="list create show"
        COMPREPLY=( $(compgen -W "${actions}" -- ${cur}) )
        return 0
    fi

    case ${action} in
        list)
            options=""
            ;;
        create)
            if [[ ${cur} == -* ]]; then
                options="--abstract --precision"
            else
                case ${prev} in
                    -a|--abstract)
                        return 1
                        ;;
                    -p|--precision)
                        return 1
                        ;;
                esac
            fi
            ;;
        show)
            if [[ ${cur} == -* ]]; then
                options="--cdl --ncml"
            else
                case ${prev} in
                    *)
                        _rsa_list_datasets
                        return $?
                        ;;
                esac
            fi
            ;;
    esac

    COMPREPLY=( $(compgen -W "${options}" -- ${cur}) )
    return 0
}

# TIMESLICE COMMANDS
function _rsa_timeslice() {
    local actions options action actiontoknum
    action="$1"
    actiontoknum="$2"

    if [ $actiontoknum -eq $COMP_CWORD ]; then
        local actions
        actions="list create"
        COMPREPLY=( $(compgen -W "${actions}" -- ${cur}) )
        return 0
    fi

    case ${action} in
        list)
            _rsa_list_datasets
            return $?
            ;;
        create)
            if [[ ${cur} == -* ]]; then
                options="--abstract"
            else
                case ${prev} in
                    -a|--abstract)
                        return 1
                        ;;
                    *)
                        _rsa_list_datasets
                        return $?
                        ;;
                esac
            fi
            ;;
    esac

    COMPREPLY=( $(compgen -W "${options}" -- ${cur}) )
    return 0
}

# BAND COMMANDS
function _rsa_band() {
    local actions options action actiontoknum
    action="$1"
    actiontoknum="$2"

    if [ $actiontoknum -eq $COMP_CWORD ]; then
        local actions
        actions="list create"
        COMPREPLY=( $(compgen -W "${actions}" -- ${cur}) )
        return 0
    fi

    case ${action} in
        list)
            _rsa_list_datasets
            return $?
            ;;
        create)
            if [[ ${cur} == -* ]]; then
                options="--continuous --metadata"
            else
                case ${prev} in
                    -m|--metadata)
                        return 1
                        ;;
                    # No special case for --continuous because it has no argument.
                    *)
                        _rsa_list_datasets
                        return $?
                        ;;
                esac
            fi
            ;;
    esac

    COMPREPLY=( $(compgen -W "${options}" -- ${cur}) )
    return 0
}

# DATA COMMANDS
function _rsa_data() {
    local actions options action actiontoknum
    action="$1"
    actiontoknum="$2"

    if [ $actiontoknum -eq $COMP_CWORD ]; then
        local actions
        actions="import export query task"
        COMPREPLY=( $(compgen -W "${actions}" -- ${cur}) )
        return 0
    fi

    options=""
    case ${action} in
        import)
            # TODO: there are more required arguments here.
            _rsa_list_files
            return $?
            ;;

        export)
            if [[ ${cur} == -* ]]; then
                options="--extents --time-extents --express --output"
            else
                case ${prev} in
                    -x|--extents)
                        return 1
                        ;;
                    -t|--time-extents)
                        return 1
                        ;;
                    # no special case for --express
                    --output)
                        _filedir
                        return $?
                        ;;
                    *)
                        _rsa_list_datasets
                        return $?
                        ;;
                esac
            fi
            ;;

        query)
            if [[ ${cur} == -* ]]; then
                options="--extents --time-extents --output"
            else
                case ${prev} in
                    -x|--extents)
                        return 1
                        ;;
                    -t|--time-extents)
                        return 1
                        ;;
                    --output)
                        _filedir
                        return 0
                        ;;
                    *)
                        _filedir xml
                        return 0
                        ;;
                esac
            fi
            ;;

        task)
            if [[ ${cur} == -* ]]; then
                options="--task-type --status"
            else
                case ${prev} in
                    --task-type)
                        options="Import Export"
                        ;;
                    --status)
                        options="RUNNING CANCELLING FINISHED INITIALISATION_ERROR EXECUTION_ERROR"
                        ;;
                esac
            fi
            ;;

        *)
            return 1
            ;;
    esac

    COMPREPLY=( $(compgen -W "${options}" -- ${cur}) )
    return 0
}

# Entry point
function _rsacli_completion() {
    local commands cmd action actiontoknum options

    COMPREPLY=()
    # Don't make these local; they are used by built-in functions like _filedir
    cur="${COMP_WORDS[COMP_CWORD]}"
    prev="${COMP_WORDS[COMP_CWORD-1]}"

    # Find command and action
    cmd=""
    action=""
    actiontoknum=-1
    i=0
    for tok in ${COMP_WORDS[@]:1}; do
        i=$(($i + 1))
        if [[ ${tok} == -* ]]; then
            continue
        fi
        if [ "x${cmd}" = "x" ]; then
            cmd=${tok}
            continue
        fi
        if [ "x${action}" = "x" ]; then
            action=${tok}
            actiontoknum=$i
            break
        fi
    done
    if [ "x${action}" = "x" ]; then
        action=${cur}
        actiontoknum=$COMP_CWORD
    fi

    #echo -e "\n${COMP_WORDS[@]}"
    #echo -e "\nCommand: $cmd, Action: $action@$actiontoknum, Current: ${cur}@$COMP_CWORD"

    if [ $COMP_CWORD -eq 1 ]; then
        # Specifying command word.
        case ${cur} in
            -*)
                options="--compile-filters --stop --start --restart --daemon --no-daemon"
                COMPREPLY=( $(compgen -W "${options}" -- ${cur}) )
                return 0
                ;;
            *)
                commands="dataset timeslice band data"
                COMPREPLY=( $(compgen -W "${commands}" -- ${cur}) )
                return 0
                ;;
        esac
    fi

    case ${cmd} in
        dataset)
            _rsa_dataset "${action}" "${actiontoknum}"
            return $?
            ;;
        timeslice)
            _rsa_timeslice "${action}" "${actiontoknum}"
            return $?
            ;;
        band)
            _rsa_band "${action}" "${actiontoknum}"
            return $?
            ;;
        data)
            _rsa_data "${action}" "${actiontoknum}"
            return $?
            ;;
    esac

    return 1
}

complete -F _rsacli_completion rsa


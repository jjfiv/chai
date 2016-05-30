/*
 * Copyright (c) 2011, Benjamin Jacob Coverston
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *     Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *     Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ciir.jfoley.chai.collections.interval;

import ciir.jfoley.chai.collections.util.IterableFns;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class IntervalNode<T>
{
    Interval<T> interval;
    Comparable v_pt;
    List<Interval<T>> v_left;
    List<Interval<T>> v_right;
    IntervalNode left = null;
    IntervalNode right = null;

    public IntervalNode(List<Interval<T>> toBisect)
    {
        if( toBisect.size() > 0 )
        {
            v_pt = findMedianEndpoint(toBisect);
            //v_left =  interval.minOrdering.sortedCopy(getIntersectingIntervals(toBisect));
            v_left = IterableFns.<Interval<T>>sorted(getIntersectingIntervals(toBisect), Interval.getMinOrdering());
            //v_right = interval.maxOrdering.reverse().sortedCopy(getIntersectingIntervals(toBisect));
            v_right = IterableFns.<Interval<T>>sorted(getIntersectingIntervals(toBisect), Interval.<T>getMaxOrdering().reversed());
            //if i.min < v_pt then it goes to the left subtree
            List<Interval<T>> leftSegment = getLeftIntervals(toBisect);
            List<Interval<T>> rightSegment = getRightIntervals(toBisect);
            if(leftSegment.size() > 0)
                this.left = new IntervalNode<>(leftSegment);
            if(rightSegment.size() > 0)
                this.right = new IntervalNode<>(rightSegment);
        }
    }

    public List<Interval<T>> getLeftIntervals(List<Interval<T>> candidates)
    {
        List<Interval<T>> retval = new ArrayList<>();
        for (Interval<T> candidate : candidates)
        {
            if(candidate.max.compareTo(v_pt) < 0 )
                retval.add(candidate);
        }
        return retval;
    }

    public List<Interval<T>> getRightIntervals(List<Interval<T>> candidates)
    {
        List<Interval<T>> retval = new ArrayList<>();
        for (Interval candidate : candidates)
        {
            if(candidate.min.compareTo(v_pt) > 0 )
                retval.add(candidate);
        }
        return retval;
    }

    public List<Interval<T>> getIntersectingIntervals(List<Interval<T>> candidates)
    {
        List<Interval<T>> retval = new ArrayList<>();
        for (Interval<T> candidate : candidates)
        {
            if (candidate.min.compareTo(v_pt) <= 0
                && candidate.max.compareTo(v_pt) >= 0)
                retval.add(candidate);
        }
        return retval;
    }

    public Comparable<Interval<T>> findMedianEndpoint(List<Interval<T>> intervals)
    {

        ConcurrentSkipListSet<Comparable<T>> sortedSet = new ConcurrentSkipListSet<>();

        for (Interval interval : intervals)
        {
            sortedSet.add(interval.min);
            sortedSet.add(interval.max);
        }
        int medianIndex = sortedSet.size()/2;
        if(sortedSet.size() > 0)
        {
            return (Comparable)sortedSet.toArray()[medianIndex];
        }
        return null;
    }

}
